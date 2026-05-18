package com.examhelper.api.question.domain

import com.examhelper.api.kernel.core.DomainEntity
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.question.domain.exception.QuestionItemAssertionException
import com.examhelper.api.question.domain.type.QuestionItemStatus
import com.examhelper.api.question.domain.vo.AnswerChoice
import com.examhelper.api.question.domain.vo.AnswerSheet
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Explanation
import com.examhelper.api.question.domain.vo.QualityScore
import com.examhelper.api.question.domain.vo.QuestionItemContent
import com.examhelper.api.question.domain.vo.QuestionItemMetadata
import java.time.Instant

class QuestionItem private constructor(
    id: QuestionItemId,
    val generationId: QuestionGenerationId,
    content: QuestionItemContent,
    answerSheet: AnswerSheet,
    val metadata: QuestionItemMetadata,
    val explanation: Explanation,
    qualityScore: QualityScore?,
    status: QuestionItemStatus,
    val createdAt: Instant,
    updatedAt: Instant,
) : DomainEntity<QuestionItemId>(id) {
    var content: QuestionItemContent = content
        private set

    var answerSheet: AnswerSheet = answerSheet
        private set

    var qualityScore: QualityScore? = qualityScore
        private set

    var status: QuestionItemStatus = status
        private set

    var updatedAt: Instant = updatedAt
        private set

    // ── 품질 점수 부여 ─────────────────────────────────────────
    fun assignQualityScore(score: QualityScore): QuestionItem {
        qualityScore = score
        updatedAt = Instant.now()
        return this
    }

    // ── 도메인 검증 ────────────────────────────────────────────
    private fun validate() { validateExhibitAnswerSheetConsistency() }

    private fun validateExhibitAnswerSheetConsistency() {
        if (content.exhibit !is Exhibit.PropositionExhibit) return
        val sheet = answerSheet as? AnswerSheet.MultipleChoiceSheet ?: return
        require(sheet.choices.all { it is AnswerChoice.PropositionCombinationChoice }) {
            throw QuestionItemAssertionException.ExhibitAnswerTypeMismatch()
        }
    }

    // ── 팩토리 ────────────────────────────────────────────────
    companion object {
        fun create(
            id: QuestionItemId,
            generationId: QuestionGenerationId,
            content: QuestionItemContent,
            answerSheet: AnswerSheet,
            metadata: QuestionItemMetadata,
            explanation: Explanation,
        ): QuestionItem {
            val now = Instant.now()
            return QuestionItem(
                id = id,
                generationId = generationId,
                content = content,
                answerSheet = answerSheet,
                metadata = metadata,
                explanation = explanation,
                qualityScore = null,
                status = QuestionItemStatus.DRAFT,
                createdAt = now,
                updatedAt = now,
            ).also { it.validate() }
        }

        fun of(
            id: QuestionItemId,
            generationId: QuestionGenerationId,
            content: QuestionItemContent,
            answerSheet: AnswerSheet,
            metadata: QuestionItemMetadata,
            explanation: Explanation,
            qualityScore: QualityScore?,
            status: QuestionItemStatus,
            createdAt: Instant,
            updatedAt: Instant,
        ): QuestionItem = QuestionItem(
            id = id,
            generationId = generationId,
            content = content,
            answerSheet = answerSheet,
            metadata = metadata,
            explanation = explanation,
            qualityScore = qualityScore,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
