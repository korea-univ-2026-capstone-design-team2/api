package com.examhelper.api.question.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.question.domain.event.QuestionCreatedEvent
import com.examhelper.api.question.domain.vo.SharedQuestionContext
import com.examhelper.api.question.domain.exception.QuestionAssertionException
import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.vo.QuestionMetadata
import java.time.Instant

class Question private constructor(
    id: QuestionId,
    val generationId: QuestionGenerationId,
    sharedContext: SharedQuestionContext?,
    questionItemIds: List<QuestionItemId>,
    val metadata: QuestionMetadata,
    status: QuestionStatus,
    val createdAt: Instant,
    updatedAt: Instant,
) : AggregateRoot<QuestionId>(id) {

    var sharedContext: SharedQuestionContext? = sharedContext
        private set

    // 순서 보장을 위해 내부는 MutableList
    private val _questionItemIds: MutableList<QuestionItemId> = questionItemIds.toMutableList()
    val questionItemIds: List<QuestionItemId> get() = _questionItemIds.toList()

    var status: QuestionStatus = status
        private set

    var updatedAt: Instant = updatedAt
        private set

    // ── 문제 편입 ──────────────────────────────────────────────
    fun addQuestion(questionItemId: QuestionItemId) {
        check(status == QuestionStatus.DRAFT) {
            throw QuestionException.CannotModifyNonDraft(status.name)
        }
        check(!_questionItemIds.contains(questionItemId)) {
            throw QuestionException.QuestionAlreadyIn(questionItemId.value)
        }

        _questionItemIds.add(questionItemId)
        updatedAt = Instant.now()
    }

    fun archive(): Question {
        check(status == QuestionStatus.PUBLISHED) {
            throw QuestionException.StatusTransitionNotAllowed(
                status.name,
                QuestionStatus.ARCHIVED.name
            )
        }

        status = QuestionStatus.ARCHIVED
        updatedAt = Instant.now()
        return this
    }

    // ── 도메인 검증 ────────────────────────────────────────────
    private fun validate() {
        require(_questionItemIds.size == _questionItemIds.distinct().size) {
            throw QuestionAssertionException.DuplicateQuestionIds()
        }
    }

    // ── 팩토리 ────────────────────────────────────────────────
    companion object {
        fun create(
            id: QuestionId,
            generationId: QuestionGenerationId,
            sharedContext: SharedQuestionContext?,
            metadata: QuestionMetadata,
        ): Question {
            val now = Instant.now()
            return Question(
                id = id,
                generationId = generationId,
                sharedContext = sharedContext,
                questionItemIds = emptyList(),          // Question들이 이후에 addQuestion()으로 편입
                metadata = metadata,
                status = QuestionStatus.DRAFT,
                createdAt = now,
                updatedAt = now,
            ).also {
                it.validate()
                it.addDomainEvent(
                    QuestionCreatedEvent(
                        groupId = id.value,
                        generationId = generationId.value,
                        subject = metadata.subject.name,
                        difficulty = metadata.difficulty.name,
                        occurredAt = now,
                    )
                )
            }
        }

        fun of(
            id: QuestionId,
            generationId: QuestionGenerationId,
            sharedContext: SharedQuestionContext?,
            questionItemIds: List<QuestionItemId>,
            metadata: QuestionMetadata,
            status: QuestionStatus,
            createdAt: Instant,
            updatedAt: Instant,
        ): Question = Question(
            id = id,
            generationId = generationId,
            sharedContext = sharedContext,
            questionItemIds = questionItemIds,
            metadata = metadata,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt,
        ).also { it.validate() }
    }
}
