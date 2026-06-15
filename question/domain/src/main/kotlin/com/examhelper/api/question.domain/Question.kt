package com.examhelper.api.question.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.MemberId
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
    val memberId: MemberId,
    val generationId: QuestionGenerationId,
    sharedContext: SharedQuestionContext?,
    items: List<QuestionItem>,
    val metadata: QuestionMetadata,
    status: QuestionStatus,
    val createdAt: Instant,
    updatedAt: Instant,
) : AggregateRoot<QuestionId>(id) {
    var sharedContext: SharedQuestionContext? = sharedContext
        private set

    private val _items: MutableList<QuestionItem> = items.toMutableList()
    val items: List<QuestionItem> get() = _items.toList()

    var status: QuestionStatus = status
        private set

    var updatedAt: Instant = updatedAt
        private set

    // ── 문제 편입 ──────────────────────────────────────────────
    fun addItem(item: QuestionItem) {
        check(status == QuestionStatus.DRAFT) { throw QuestionAssertionException.CannotModifyNonDraft(status.name) }

        check(_items.none { it.id == item.id }) {
            throw QuestionAssertionException.QuestionAlreadyIn(item.id.value)
        }

        _items.add(item)
        updatedAt = Instant.now()
    }

    // ── 도메인 검증 ────────────────────────────────────────────
    private fun validate() {
        require(_items.size == _items.distinct().size) {
            throw QuestionAssertionException.DuplicateQuestionIds()
        }
    }

    // ── 팩토리 ────────────────────────────────────────────────
    companion object {
        fun create(
            id: QuestionId,
            memberId: MemberId,
            generationId: QuestionGenerationId,
            sharedContext: SharedQuestionContext?,
            metadata: QuestionMetadata,
        ): Question {
            val now = Instant.now()
            return Question(
                id = id,
                memberId = memberId,
                generationId = generationId,
                sharedContext = sharedContext,
                items = emptyList(),
                metadata = metadata,
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
                        difficulty = metadata.difficulty.name,
                        occurredAt = now,
                    )
                )
            }
        }

        fun of(
            id: QuestionId,
            memberId: MemberId,
            generationId: QuestionGenerationId,
            sharedContext: SharedQuestionContext?,
            items: List<QuestionItem>,
            metadata: QuestionMetadata,
            status: QuestionStatus,
            createdAt: Instant,
            updatedAt: Instant,
        ): Question = Question(
            id = id,
            memberId = memberId,
            generationId = generationId,
            sharedContext = sharedContext,
            items = items,
            metadata = metadata,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        ).also { it.validate() }
    }
}
