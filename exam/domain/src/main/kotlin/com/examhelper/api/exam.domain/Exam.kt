package com.examhelper.api.exam.domain

import com.examhelper.api.exam.domain.entity.ExamItem
import com.examhelper.api.exam.domain.event.ExamCreatedEvent
import com.examhelper.api.exam.domain.event.ExamGenerationCompletedEvent
import com.examhelper.api.exam.domain.event.ExamGenerationFailedEvent
import com.examhelper.api.exam.domain.exception.ExamAssertionException
import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.domain.vo.ExamMetadata
import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import java.time.Instant
import kotlin.collections.toMutableList

class Exam private constructor(
    id: ExamId,
    val memberId: MemberId,
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

    fun startGeneration(generationId: QuestionGenerationId) {
        check(status == ExamStatus.GENERATING) {
            throw ExamAssertionException.StatusTransitionNotAllowed(status.name, ExamStatus.GENERATING.name)
        }
        check(generationResult == null) {
            throw ExamAssertionException.GenerationAlreadyStarted(generationResult?.generationId?.value ?: -1L)
        }

        generationResult = ExamGenerationResult(
            generationId = generationId,
            successCount = null,
            failCount = null,
        )
    }

    // ── 문항 편입 ──────────────────────────────────────────────
    fun addItem(item: ExamItem) {
        check(status == ExamStatus.GENERATING || status == ExamStatus.GENERATION_FINISHED) {
            throw ExamAssertionException.CannotModifyItems(status.name)
        }

        if (_items.any { it.questionId == item.questionId }) return

        _items.add(item)
        updatedAt = Instant.now()

        tryComplete()
    }

    fun markGenerationFinished(result: ExamGenerationResult) {
        require(!result.isPending()) { throw ExamAssertionException.PendingGenerationResult() }

        if (status == ExamStatus.GENERATION_FINISHED || status == ExamStatus.READY) {
            if (generationResult?.generationId == result.generationId) return
        }

        check(status == ExamStatus.GENERATING) {
            throw ExamAssertionException.StatusTransitionNotAllowed(
                status.name,
                ExamStatus.GENERATION_FINISHED.name,
            )
        }
        check(generationResult?.generationId == result.generationId) {
            throw ExamAssertionException.GenerationIdMismatch(
                expected = generationResult?.generationId?.value ?: -1L,
                actual = result.generationId?.value ?: -1L,
            )
        }

        generationResult = result
        transitionTo(ExamStatus.GENERATION_FINISHED)

        tryComplete()
    }

    private fun tryComplete() {
        if (status != ExamStatus.GENERATION_FINISHED) return

        val result = generationResult ?: return
        val successCount = result.successCount ?: return

        val distinctItemCount = _items.distinctBy { it.questionId }.size
        if (distinctItemCount < successCount) return

        transitionTo(ExamStatus.READY)

        addDomainEvent(
            ExamGenerationCompletedEvent(
                examId = id.value,
                itemCount = distinctItemCount,
                successCount = successCount,
                failCount = result.failCount ?: 0,
                occurredAt = Instant.now(),
            )
        )
    }

    fun completeGeneration(result: ExamGenerationResult) {
        require(!result.isPending()) { throw ExamAssertionException.PendingGenerationResult() }
        check(status == ExamStatus.GENERATING) {
            throw ExamAssertionException.StatusTransitionNotAllowed(status.name, ExamStatus.READY.name)
        }
        require(isEmptyExam() || result.successCount == 0) {
            throw ExamAssertionException.InvalidImmediateCompletion()
        }

        generationResult = result
        transitionTo(ExamStatus.READY)

        addDomainEvent(
            ExamGenerationCompletedEvent(
                examId = id.value,
                itemCount = _items.size,
                successCount = result.successCount ?: 0,
                failCount = result.failCount ?: 0,
                occurredAt = updatedAt,
            )
        )
    }

    fun failGeneration(reason: String) {
        if (status == ExamStatus.FAILED) return

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

    fun isEmptyExam(): Boolean = metadata.targetQuestionCount == 0

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
            memberId: MemberId,
            title: String,
            metadata: ExamMetadata,
        ): Exam {
            val now = Instant.now()
            return Exam(
                id = id,
                memberId = memberId,
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
            memberId: MemberId,
            title: String,
            metadata: ExamMetadata,
            status: ExamStatus,
            items: List<ExamItem>,
            generationResult: ExamGenerationResult?,
            createdAt: Instant,
            updatedAt: Instant,
        ): Exam = Exam(
            id = id,
            memberId = memberId,
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
