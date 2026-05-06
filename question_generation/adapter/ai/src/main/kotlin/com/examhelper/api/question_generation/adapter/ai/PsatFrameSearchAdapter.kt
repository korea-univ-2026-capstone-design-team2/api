package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.question_generation.adapter.ai.exception.FrameSearchException
import com.examhelper.api.question_generation.port.outbound.FrameSearchPort
import com.examhelper.api.question_generation.port.outbound.query.FrameSearchQuery
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Component

@Component
class PsatFrameSearchAdapter(
    private val vectorStore: VectorStore
) : FrameSearchPort {
    override fun search(query: FrameSearchQuery): List<FrameSearchResult> {
        val filterExpr = buildFilterExpression(query)
        val queryText = buildQueryText(query)

        val documents = try {
            vectorStore.similaritySearch(
                SearchRequest.builder()
                    .query(queryText)
                    .topK(query.topK)
                    .similarityThreshold(SCORE_THRESHOLD.toDouble())
                    .filterExpression(filterExpr)
                    .build()
            )
        } catch (ex: Exception) {
            throw FrameSearchException.QdrantUnavailable(cause = ex, collection = "")
        }

        return documents.map { it.toFrameSearchResult() }
    }

    private fun buildQueryText(query: FrameSearchQuery): String = buildString {
        appendLine("[논리구조] ${query.questionType.name}")
        query.questionSubType?.let { appendLine("[하위유형] ${it.name}") }
        appendLine("[난이도] ${query.difficulty.name}")
        appendLine("[주제] ${query.topic.category}")
        query.topic.keyword?.let { appendLine("[키워드] $it") }
    }

    private fun buildFilterExpression(query: FrameSearchQuery): String {
        val conditions = mutableListOf<String>()

        conditions += "question_type == '${query.questionType.name}'"
        conditions += "difficulty == '${query.difficulty.name}'"
        query.questionSubType?.let {
            conditions += "question_sub_type == '${it.name}'"
        }

        return conditions.joinToString(" && ")
    }

    private fun Document.toFrameSearchResult(): FrameSearchResult {
        return FrameSearchResult(
            frameId = id,
            similarityScore = this.score ?: 0.0,

            questionType = safeEnum<QuestionType>(metadata["question_type"])
                ?: throw FrameSearchException.PayloadFieldMissing("question_type"),

            questionSubType = safeEnum<QuestionSubType>(
                metadata["question_sub_type"]
            ),

            difficulty = safeEnum<DifficultyLevel>(metadata["difficulty"])
                ?: throw FrameSearchException.PayloadFieldMissing("difficulty"),

            abstractPassage = metadata.getStringOrNull("abstract_passage"),
            logicalStructureSummary = metadata.getString("logical_structure_summary"),
            argumentPattern = metadata.getStringOrNull("argument_pattern"),
            stemTemplate = metadata.getString("stem_template"),
            choicePattern = metadata.getString("choice_pattern"),
            originalStem = metadata.getString("original_stem"),
            originalPassage = metadata.getStringOrNull("original_passage"),
            originalChoices = metadata.getStringList("original_choices"),
            originalExplanation = metadata.getString("original_explanation"),
        )
    }

    private fun Map<String, Any>.getString(key: String): String =
        this[key] as? String
            ?: throw FrameSearchException.PayloadFieldMissing(key)

    private fun Map<String, Any>.getStringOrNull(key: String): String? =
        this[key] as? String

    private fun Map<String, Any>.getStringList(key: String): List<String> =
        (this[key] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

    private inline fun <reified T : Enum<T>> safeEnum(value: Any?): T? {
        val str = value as? String ?: return null
        return enumValues<T>().find { it.name == str }
    }

    companion object {
        private const val COLLECTION_NAME = "psat_frames"
        private const val SCORE_THRESHOLD = 0.70f
    }
}
