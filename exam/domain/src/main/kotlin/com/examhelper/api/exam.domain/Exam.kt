package com.examhelper.api.exam.domain

import com.examhelper.api.exam.domain.entity.ExamItem
import com.examhelper.api.exam.domain.event.ExamCreatedEvent
import com.examhelper.api.exam.domain.event.ExamGenerationCompletedEvent
import com.examhelper.api.exam.domain.event.ExamGenerationFailedEvent
import com.examhelper.api.exam.domain.exception.ExamAssertionException
import com.examhelper.api.exam.domain.exception.ExamException
import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.domain.vo.ExamMetadata
import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.ExamId
import java.time.Instant
import kotlin.collections.toMutableList

class Exam private constructor(
    id: ExamId,
    val title: String,
    val metadata: ExamMetadata,
    status: ExamStatus,
    items: List<ExamItem>,
    generationResult: ExamGenerationResult?,
    val createdAt: Instant,
    updatedAt: Instant,
) : AggregateRoot<ExamId>(id) {
    var status: ExamStatus = status
        private set

    private val _items: MutableList<ExamItem> = items.toMutableList()
    val items: List<ExamItem> get() = _items.toList()

    var generationResult: ExamGenerationResult? = generationResult
        private set

    var updatedAt: Instant = updatedAt
        private set

    // ── 문항 편입 ──────────────────────────────────────────────
    fun addItem(item: ExamItem) {
        if (status != ExamStatus.GENERATING) { throw ExamAssertionException.CannotModifyItems(status.name) }

        if (_items.any { it.id == item.id }) { throw ExamAssertionException.ItemAlreadyExists(item.id.value) }

        _items.add(item)
        updatedAt = Instant.now()
    }

    // ── 상태 전이 ──────────────────────────────────────────────
    fun completeGeneration(result: ExamGenerationResult) {
        check(status == ExamStatus.GENERATING) {
            throw ExamAssertionException.StatusTransitionNotAllowed(status.name, ExamStatus.READY.name)
        }

        generationResult = result
        transitionTo(ExamStatus.READY)

        addDomainEvent(
            ExamGenerationCompletedEvent(
                examId = id.value,
                itemCount = _items.size,
                successCount = result.successCount,
                failCount = result.failCount,
                occurredAt = updatedAt,
            )
        )
    }

    fun failGeneration(reason: String) {
        check(status == ExamStatus.GENERATING) {
            throw ExamAssertionException.StatusTransitionNotAllowed(status.name, ExamStatus.FAILED.name)
        }
        transitionTo(ExamStatus.FAILED)

        addDomainEvent(
            ExamGenerationFailedEvent(
                examId = id.value,
                reason = reason,
                occurredAt = updatedAt,
            )
        )
    }

    // ── 도메인 검증 ────────────────────────────────────────────
    private fun validate() {
        require(_items.size == _items.distinctBy { it.id }.size) { throw ExamAssertionException.DuplicateItemIds() }
    }

    // ── 내부 ───────────────────────────────────────────────────
    private fun transitionTo(next: ExamStatus) {
        status = next
        updatedAt = Instant.now()
    }

    // ── 팩토리 ─────────────────────────────────────────────────
    companion object {
        fun create(
            id: ExamId,
            title: String,
            metadata: ExamMetadata,
        ): Exam {
            val now = Instant.now()
            return Exam(
                id = id,
                title = title,
                metadata = metadata,
                status = ExamStatus.GENERATING,
                items = emptyList(),
                generationResult = null,
                createdAt = now,
                updatedAt = now,
            ).also {
                it.validate()
                it.addDomainEvent(
                    ExamCreatedEvent(
                        examId = id.value,
                        subject = metadata.subject.name,
                        questionType = metadata.questionType.name,
                        difficulty = metadata.difficulty.name,
                        targetQuestionCount = metadata.targetQuestionCount,
                        occurredAt = now,
                    )
                )
            }
        }

        fun of(
            id: ExamId,
            title: String,
            metadata: ExamMetadata,
            status: ExamStatus,
            items: List<ExamItem>,
            generationResult: ExamGenerationResult?,
            createdAt: Instant,
            updatedAt: Instant,
        ): Exam = Exam(
            id = id,
            title = title,
            metadata = metadata,
            status = status,
            items = items,
            generationResult = generationResult,
            createdAt = createdAt,
            updatedAt = updatedAt,
        ).also { it.validate() }
    }
}
