package com.examhelper.api.question.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.question.domain.event.QuestionPublishedEvent
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionSetId
import com.examhelper.api.question.domain.event.QuestionCreatedEvent
import com.examhelper.api.question.domain.event.QuestionRejectedEvent
import com.examhelper.api.question.domain.event.QuestionSetAssignedEvent
import com.examhelper.api.question.domain.exception.QuestionAssertionException
import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.vo.AnswerChoice
import com.examhelper.api.question.domain.vo.AnswerSheet
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Explanation
import com.examhelper.api.question.domain.vo.FrameReference
import com.examhelper.api.question.domain.vo.QualityScore
import com.examhelper.api.question.domain.vo.QuestionContent
import com.examhelper.api.question.domain.vo.QuestionMetadata
import java.time.Instant

class Question private constructor(
    id: QuestionId,
    val generationId: QuestionGenerationId,
    content: QuestionContent,
    answerSheet: AnswerSheet,
    val metadata: QuestionMetadata,
    val explanation: Explanation,
    val sourceFrame: FrameReference,
    qualityScore: QualityScore?,
    questionSetId: QuestionSetId?,
    status: QuestionStatus,
    val createdAt: Instant,
    updatedAt: Instant,
) : AggregateRoot<QuestionId>(id) {

    var content: QuestionContent = content
        private set

    var answerSheet: AnswerSheet = answerSheet
        private set

    var qualityScore: QualityScore? = qualityScore
        private set

    var questionSetId: QuestionSetId? = questionSetId
        private set

    var status: QuestionStatus = status
        private set

    var updatedAt: Instant = updatedAt
        private set

    // ── 상태 전이 ──────────────────────────────────────────────
    fun publish(): Question {
        val score = qualityScore
            ?: throw QuestionException.QualityScoreNotAssigned()
        if (!score.isPassing())
            throw QuestionException.QualityScoreTooLowToPublish(score.value)
        if (status != QuestionStatus.DRAFT)
            throw QuestionException.StatusTransitionNotAllowed(status.name, QuestionStatus.PUBLISHED.name)

        status = QuestionStatus.PUBLISHED
        updatedAt = Instant.now()
        addDomainEvent(QuestionPublishedEvent(
            questionId = id.value,
            subject = metadata.subject.name,
            questionType = metadata.questionType.name,
            difficulty = metadata.difficulty.name,
            occurredAt = updatedAt,
        ))
        return this
    }

    fun reject(): Question {
        if (status != QuestionStatus.DRAFT)
            throw QuestionException.StatusTransitionNotAllowed(status.name, QuestionStatus.REJECTED.name)

        status = QuestionStatus.REJECTED
        updatedAt = Instant.now()
        addDomainEvent(QuestionRejectedEvent(
            questionId = id.value,
            generationId = generationId.value,
            occurredAt = updatedAt,
        ))
        return this
    }

    // ── 품질 점수 부여 ─────────────────────────────────────────
    fun assignQualityScore(score: QualityScore): Question {
        qualityScore = score
        updatedAt = Instant.now()
        return this
    }

    // ── 세트 편입 ──────────────────────────────────────────────
    fun assignToSet(setId: QuestionSetId): Question {
        if (questionSetId != null)
            throw QuestionException.QuestionSetAlreadyAssigned(questionSetId!!.value)

        questionSetId = setId
        updatedAt = Instant.now()
        addDomainEvent(QuestionSetAssignedEvent(
            questionId = id.value,
            questionSetId = setId.value,
            occurredAt = updatedAt,
        ))
        return this
    }

    // ── 도메인 검증 ────────────────────────────────────────────
    private fun validate() {
        validateExhibitAnswerSheetConsistency()
        validatePassageTopicConsistency()
    }

    // PropositionExhibit이면 선지도 반드시 PropositionCombinationChoice여야 함
    private fun validateExhibitAnswerSheetConsistency() {
        if (content.exhibit !is Exhibit.PropositionExhibit) return
        val sheet = answerSheet as? AnswerSheet.MultipleChoiceSheet ?: return
        require(sheet.choices.all { it is AnswerChoice.PropositionCombinationChoice }) {
            throw QuestionAssertionException.ExhibitAnswerTypeMismatch()
        }
    }

    // 지문이 없으면 passageTopic도 없어야 함
    private fun validatePassageTopicConsistency() {
        require(content.passage != null || metadata.passageTopic == null) {
            throw QuestionAssertionException.PassageTopicWithoutPassage()
        }
    }

    // ── 팩토리 ────────────────────────────────────────────────
    companion object {
        fun create(
            id: QuestionId,
            generationId: QuestionGenerationId,
            content: QuestionContent,
            answerSheet: AnswerSheet,
            metadata: QuestionMetadata,
            explanation: Explanation,
            sourceFrame: FrameReference,
        ): Question {
            val now = Instant.now()
            return Question(
                id = id,
                generationId = generationId,
                content = content,
                answerSheet = answerSheet,
                metadata = metadata,
                explanation = explanation,
                sourceFrame = sourceFrame,
                qualityScore = null,
                questionSetId = null,
                status = QuestionStatus.DRAFT,
                createdAt = now,
                updatedAt = now,
            ).also {
                it.validate()
                it.addDomainEvent(
                    QuestionCreatedEvent(
                        questionId = id.value,
                        generationId = generationId.value,
                        subject = metadata.subject.name,
                        questionType = metadata.questionType.name,
                        questionSubType = metadata.questionSubType?.name,
                        difficulty = metadata.difficulty.name,
                        occurredAt = now,
                    )
                )
            }
        }

        fun of(
            id: QuestionId,
            generationId: QuestionGenerationId,
            content: QuestionContent,
            answerSheet: AnswerSheet,
            metadata: QuestionMetadata,
            explanation: Explanation,
            sourceFrame: FrameReference,
            qualityScore: QualityScore?,
            questionSetId: QuestionSetId?,
            status: QuestionStatus,
            createdAt: Instant,
            updatedAt: Instant,
        ): Question = Question(
            id = id,
            generationId = generationId,
            content = content,
            answerSheet = answerSheet,
            metadata = metadata,
            explanation = explanation,
            sourceFrame = sourceFrame,
            qualityScore = qualityScore,
            questionSetId = questionSetId,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}
