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
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.qdrant.autoconfigure.QdrantVectorStoreProperties
import org.springframework.stereotype.Component

@Component
class PsatFrameSearchAdapter(
    private val vectorStore: VectorStore,
    private val frameSearchProperties: FrameSearchProperties,
    private val qdrantProperties : QdrantVectorStoreProperties,
    private val metrics: FrameSearchMetrics
) : FrameSearchPort {
    override fun search(query: FrameSearchQuery): List<FrameSearchResult> {
        return metrics.searchTimer.recordCallable {

            val queryText = buildQueryText(query)
            val filterExpression = buildFilterExpression(query)

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
                    collection = qdrantProperties.collectionName
                )
            }

            documents.map { it.toFrameSearchResult() }
        }
    }

    // retrieval_text 와 최대한 동일한 구조로 query 생성
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
        appendLine(
            when (query.questionSubType) {
                QuestionSubType.MATCH -> "직접 진술과 선택지를 비교하여 일치 여부 판단"
                QuestionSubType.INFERENCE -> "명시 정보 기반 추론 가능 여부 판단"
                QuestionSubType.BLANK_FILLING -> "문맥 흐름 기반 빈칸 추론"
                QuestionSubType.KNOWABLE -> "지문 정보만으로 판단 가능 여부 검증"
                QuestionSubType.CONTEXT_CORRECTION -> "문맥 흐름 및 논리 관계 교정"
                QuestionSubType.CORE_ARGUMENT -> "핵심 주장 및 중심 논지 식별"
                QuestionSubType.ARGUMENT_ANALYSIS -> "논증 구조와 전제-결론 관계 분석"
                QuestionSubType.STRENGTHEN_WEAKEN -> "논증 강화 및 약화 요소 분석"
                null -> when (query.questionType) {
                    QuestionType.READING     -> "독해 기반 정보 추출 및 이해"
                    QuestionType.ARGUMENTATION    -> "논증 구조 및 전제-결론 관계 분석"
                    QuestionType.LOGIC_PUZZLE  -> "조건 관계 기반 논리 퍼즐 풀이"
                }
            }
        )
    }

    // payload index 기반 filter
    private fun buildFilterExpression(query: FrameSearchQuery): String {
        val conditions = mutableListOf<String>()
        conditions += "question_type == '${query.questionType.name}'"
        query.questionSubType?.let { conditions += "question_sub_type == '${it.name}'" }
        conditions += "difficulty == '${query.difficulty.name}'"
        conditions += "topic_category == '${query.topic.category.name}'"

        return conditions.joinToString(" && ")
    }

    private fun Document.toFrameSearchResult(): FrameSearchResult {
        return FrameSearchResult(
            frameId = id,
            similarityScore = this.score ?: 0.0,

            questionType = safeEnum<QuestionType>(metadata["question_type"])
                ?: throw FrameSearchException.PayloadFieldMissing("question_type"),
            questionSubType = safeEnum<QuestionSubType>(metadata["question_sub_type"]),
            difficulty = safeEnum<DifficultyLevel>(metadata["difficulty"])
                ?: throw FrameSearchException.PayloadFieldMissing("difficulty"),
            topicCategory = safeEnum<TopicCategory>(metadata["topic_category"])
                ?: throw FrameSearchException.PayloadFieldMissing("topic_category"),
            topicKeyword = metadata.getStringOrNull("topic_keyword"),

            reasoningType = metadata.getString("reasoning_type"),
            premises = metadata.getStringList("premises"),
            conditions = metadata.getStringList("conditions"),
            logicalGoal = metadata.getString("logical_goal"),
            inferenceStructure = metadata.getStringList("inference_structure"),

            reasoningPatterns = metadata.getStringList("reasoning_patterns"),
            trapPatterns = metadata.getStringList("trap_patterns"),
            discourseStructure = metadata.getStringList("discourse_structure"),
            cognitiveOperations = metadata.getStringList("cognitive_operations"),
            reasoningComplexity = metadata.getString("reasoning_complexity"),

            questionStem = metadata.getString("question_stem"),
            passage = metadata.getStringOrNull("passage"),
            passageDescription = metadata.getStringOrNull("passage_description"),
            answerChoices = metadata.getStringList("answer_choices"),
            correctAnswer = metadata.getInt("correct_answer"),
            correctReason = metadata.getString("correct_reason"),

            mustPreserve = metadata.getStringList("must_preserve"),
            variableElements = metadata.getStringList("variable_elements"),

            retrievalText = metadata.getString("retrieval_text"),
        )
    }

    private fun Map<String, Any>.getString(key: String): String =
        this[key] as? String ?: throw FrameSearchException.PayloadFieldMissing(key)

    private fun Map<String, Any>.getStringOrNull(key: String): String? = this[key] as? String

    private fun Map<String, Any>.getStringList(key: String): List<String> =
        (this[key] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

    private fun Map<String, Any>.getInt(key: String): Int = when (val value = this[key]) {
            is Int -> value
            is Long -> value.toInt()
            is Double -> value.toInt()
            else -> throw FrameSearchException.PayloadFieldMissing(key)
        }

    private inline fun <reified T : Enum<T>> safeEnum(value: Any?): T? {
        val str = value as? String ?: return null

        return enumValues<T>().find { it.name == str }
    }
}
