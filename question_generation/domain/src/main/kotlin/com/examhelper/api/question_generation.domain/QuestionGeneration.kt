package com.examhelper.api.question_generation.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.question_generation.domain.event.GenerationCompletedEvent
import com.examhelper.api.question_generation.domain.event.GenerationFailedEvent
import com.examhelper.api.question_generation.domain.event.GenerationRequestedEvent
import com.examhelper.api.question_generation.domain.exception.GenerationException
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStatus
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import java.time.Instant

class QuestionGeneration private constructor(
    id: QuestionGenerationId,
    val request: QuestionGenerationRequest,
    status: QuestionGenerationStatus,
    failureReason: String?,
    val createdAt: Instant,
    updatedAt: Instant,
) : AggregateRoot<QuestionGenerationId>(id) {

    var status: QuestionGenerationStatus = status
        private set

    var failureReason: String? = failureReason
        private set

    var updatedAt: Instant = updatedAt
        private set

    // ── 상태 전이 ─────────────────────────────────────────────

    fun complete(): QuestionGeneration {
        checkPending()
        status    = QuestionGenerationStatus.COMPLETED
        updatedAt = Instant.now()
        addDomainEvent(
            GenerationCompletedEvent(
                generationId = id.value,
                occurredAt = updatedAt,
            )
        )
        return this
    }

    fun fail(reason: String): QuestionGeneration {
        checkPending()
        status        = QuestionGenerationStatus.FAILED
        failureReason = reason
        updatedAt     = Instant.now()
        addDomainEvent(
            GenerationFailedEvent(
                generationId       = id.value,
                reason             = reason,
                occurredAt         = updatedAt,
            )
        )
        return this
    }

    // ── 내부 가드 ─────────────────────────────────────────────

    private fun checkPending() {
        check(status == QuestionGenerationStatus.PENDING) {
            throw GenerationException.AlreadyTerminated(id.value, status.name)
        }
    }

    // ── 팩토리 ───────────────────────────────────────────────

    companion object {
        fun create(
            id: QuestionGenerationId,
            request: QuestionGenerationRequest,
        ): QuestionGeneration {
            val now = Instant.now()
            return QuestionGeneration(
                id                   = id,
                request              = request,
                status               = QuestionGenerationStatus.PENDING,
                failureReason        = null,
                createdAt            = now,
                updatedAt            = now,
            ).also {
                it.addDomainEvent(
                    GenerationRequestedEvent(
                        generationId = id.value,
                        subject      = request.subject.name,
                        questionType = request.questionType.name,
                        quantity     = request.quantity,
                        occurredAt   = now,
                    )
                )
            }
        }

        fun of(
            id: QuestionGenerationId,
            request: QuestionGenerationRequest,
            status: QuestionGenerationStatus,
            failureReason: String?,
            createdAt: Instant,
            updatedAt: Instant,
        ): QuestionGeneration = QuestionGeneration(
            id                   = id,
            request              = request,
            status               = status,
            failureReason        = failureReason,
            createdAt            = createdAt,
            updatedAt            = updatedAt,
        )
    }
}
