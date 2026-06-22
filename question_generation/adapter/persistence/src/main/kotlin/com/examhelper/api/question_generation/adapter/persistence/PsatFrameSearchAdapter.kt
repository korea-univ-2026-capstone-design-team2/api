package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.TopicCategory
import com.examhelper.api.question_generation.adapter.persistence.config.FrameSearchProperties
import com.examhelper.api.question_generation.adapter.persistence.exception.FrameSearchException
import com.examhelper.api.question_generation.adapter.persistence.metrics.FrameSearchMetrics
import com.examhelper.api.question_generation.port.outbound.FrameSearchPort
import com.examhelper.api.question_generation.port.outbound.query.FrameSearchQuery
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.qdrant.autoconfigure.QdrantVectorStoreProperties
import org.springframework.stereotype.Component

@Component
class PsatFrameSearchAdapter(
    private val vectorStore: VectorStore,
    private val frameSearchProperties: FrameSearchProperties,
    private val qdrantProperties: QdrantVectorStoreProperties,
    private val metrics: FrameSearchMetrics,
) : FrameSearchPort {
    private val logger = KotlinLogging.logger {}

    override suspend fun search(query: FrameSearchQuery): List<FrameSearchResult> =
        withContext(Dispatchers.IO) {
            metrics.searchTimer.recordCallable {
                val queryText = buildQueryText(query)
                searchWithFallback(query, queryText)
            }
        }

    // ── Fallback 전략 ──────────────────────────────────────────
    private fun searchWithFallback(
        query: FrameSearchQuery,
        queryText: String,
    ): List<FrameSearchResult> {
        val minDesiredResults = (query.topK / 2).coerceAtLeast(1)

        var lastResults: List<FrameSearchResult> = emptyList()

        for (step in FilterStep.entries) {
            val filter = buildFilterExpression(query, step)
            val results = executeSearch(query, queryText, filter)

            if (results.size >= minDesiredResults) return results

            if (results.size > lastResults.size) lastResults = results

            logger.warn {
                "프레임 검색 결과 부족 - 다음 단계로 완화: step=${step.name}, " +
                        "found=${results.size}, minDesired=$minDesiredResults, " +
                        "subType=${query.questionSubType}, topic=${query.topic.category}"
            }
        }

        return lastResults
    }

    private enum class FilterStep { FULL, WITHOUT_TOPIC }

    // ── 검색 실행 ──────────────────────────────────────────────
    private fun executeSearch(
        query: FrameSearchQuery,
        queryText: String,
        filterExpression: String,
    ): List<FrameSearchResult> {
        logger.info { "[FRAME_SEARCH] filter=$filterExpression | topK=${query.topK}" }

        val documents = try {
            vectorStore.similaritySearch(
                SearchRequest.builder()
                    .query(queryText)
                    .topK(query.topK)
                    .similarityThreshold(frameSearchProperties.scoreThreshold)
                    .filterExpression(filterExpression)
                    .build()
            )
        } catch (ex: Exception) {
            throw FrameSearchException.QdrantUnavailable(
                cause = ex,
                collection = qdrantProperties.collectionName,
            )
        }

        logger.info { "[FRAME_SEARCH] 결과: ${documents.size}건" }
        return documents.map { it.toFrameSearchResult() }
    }

    // ── 필터 표현식 ────────────────────────────────────────────
    private fun buildFilterExpression(query: FrameSearchQuery, step: FilterStep): String =
        buildList {
            add("question_type == '${query.questionType.name}'")
            query.questionSubType?.let { add("question_sub_type == '${it.name}'") }
            add("difficulty == '${query.difficulty.name}'")
        }.joinToString(" && ")

    // ── 쿼리 텍스트 ────────────────────────────────────────────
    private fun buildQueryText(query: FrameSearchQuery): String = buildString {
        appendLine("[QUESTION_TYPE]")
        appendLine(query.questionType.name)

        query.questionSubType?.let {
            appendLine("[QUESTION_SUBTYPE]")
            appendLine(it.name)
        }

        appendLine("[DIFFICULTY]")
        appendLine(query.difficulty.name)

        appendLine("[TOPIC_CATEGORY]")
        appendLine(query.topic.category)

        query.topic.keyword?.let {
            appendLine("[TOPIC_KEYWORD]")
            appendLine(it)
        }

        appendLine("[TASK]")
        appendLine(query.questionSubType.toTaskDescription(query.questionType))
    }

    // ── Document → FrameSearchResult ──────────────────────────
    private fun Document.toFrameSearchResult(): FrameSearchResult =
        FrameSearchResult(
            frameId = id,
            similarityScore = score ?: 0.0,
            questionType = metadata.requireEnum<QuestionType>("question_type"),
            questionSubType = metadata.safeEnum<QuestionSubType>("question_sub_type"),
            difficulty = metadata.requireEnum<DifficultyLevel>("difficulty"),
            topicCategory = metadata.requireEnum<TopicCategory>("topic_category"),
            topicKeyword = metadata.getStringOrNull("topic_keyword"),
            reasoningType = metadata.requireString("reasoning_type"),
            premises = metadata.getStringList("premises"),
            conditions = metadata.getStringList("conditions"),
            logicalGoal = metadata.requireString("logical_goal"),
            inferenceStructure = metadata.getStringList("inference_structure"),
            reasoningPatterns = metadata.getStringList("reasoning_patterns"),
            trapPatterns = metadata.getStringList("trap_patterns"),
            discourseStructure = metadata.getStringList("discourse_structure"),
            cognitiveOperations = metadata.getStringList("cognitive_operations"),
            reasoningComplexity = metadata.requireString("reasoning_complexity"),
            questionStem = metadata.requireString("question_stem"),
            passage = metadata.getStringOrNull("passage"),
            passageDescription = metadata.getStringOrNull("passage_description"),
            answerChoices = metadata.getStringList("answer_choices"),
            correctAnswer = metadata.requireInt("correct_answer"),
            correctReason = metadata.requireString("correct_reason"),
            mustPreserve = metadata.getStringList("must_preserve"),
            variableElements = metadata.getStringList("variable_elements"),
            retrievalText = metadata.requireString("retrieval_text"),
        )

    // ── Map 확장 ───────────────────────────────────────────────
    private fun Map<String, Any>.requireString(key: String): String =
        this[key] as? String ?: throw FrameSearchException.PayloadFieldMissing(key)

    private fun Map<String, Any>.getStringOrNull(key: String): String? =
        this[key] as? String

    private fun Map<String, Any>.getStringList(key: String): List<String> =
        (this[key] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

    private fun Map<String, Any>.requireInt(key: String): Int =
        when (val v = this[key]) {
            is Int    -> v
            is Long   -> v.toInt()
            is Double -> v.toInt()
            else      -> throw FrameSearchException.PayloadFieldMissing(key)
        }

    private inline fun <reified T : Enum<T>> Map<String, Any>.requireEnum(key: String): T =
        safeEnum<T>(key) ?: throw FrameSearchException.PayloadFieldMissing(key)

    private inline fun <reified T : Enum<T>> Map<String, Any>.safeEnum(key: String): T? {
        val str = this[key] as? String ?: return null
        return enumValues<T>().find { it.name == str }
    }
}

// ── QuestionSubType 확장 ───────────────────────────────────────
private fun QuestionSubType?.toTaskDescription(questionType: QuestionType): String =
    when (this) {
        QuestionSubType.MATCH              -> "직접 진술과 선택지를 비교하여 일치 여부 판단"
        QuestionSubType.INFERENCE          -> "명시 정보 기반 추론 가능 여부 판단"
        QuestionSubType.BLANK_FILLING      -> "문맥 흐름 기반 빈칸 추론"
        QuestionSubType.KNOWABLE           -> "지문 정보만으로 판단 가능 여부 검증"
        QuestionSubType.CONTEXT_CORRECTION -> "문맥 흐름 및 논리 관계 교정"
        QuestionSubType.CORE_ARGUMENT      -> "핵심 주장 및 중심 논지 식별"
        QuestionSubType.ARGUMENT_ANALYSIS  -> "논증 구조와 전제-결론 관계 분석"
        QuestionSubType.STRENGTHEN_WEAKEN  -> "논증 강화 및 약화 요소 분석"
        null -> when (questionType) {
            QuestionType.READING       -> "독해 기반 정보 추출 및 이해"
            QuestionType.ARGUMENTATION -> "논증 구조 및 전제-결론 관계 분석"
            QuestionType.LOGIC_PUZZLE  -> "조건 관계 기반 논리 퍼즐 풀이"
        }
    }
