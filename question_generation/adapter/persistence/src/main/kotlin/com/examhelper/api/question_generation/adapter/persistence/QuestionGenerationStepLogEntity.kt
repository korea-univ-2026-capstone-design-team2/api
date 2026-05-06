package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionGenerationStepLogId
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStep
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStepStatus
import com.examhelper.api.question_generation.port.inbound.model.QuestionGenerationStepLog
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name    = "generation_step_logs",
    indexes = [
        Index(name = "idx_gsl_generation_id", columnList = "generation_id"),
    ]
)
class QuestionGenerationStepLogEntity(
    @Id
    val id: Long,                                   // Snowflake

    @Column(name = "generation_id", nullable = false)
    val generationId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val step: QuestionGenerationStep,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val status: QuestionGenerationStepStatus,

    @Column(name = "duration_ms")
    val durationMs: Long?,

    @Column(length = 10000)
    val detail: String?,

    @Column(name = "occurred_at", nullable = false)
    val occurredAt: Instant
) {
    companion object {
        fun fromDomain(domain: QuestionGenerationStepLog): QuestionGenerationStepLogEntity =
            QuestionGenerationStepLogEntity(
                id           = domain.id.value,
                generationId = domain.generationId.value,
                step         = domain.step,
                status       = domain.status,
                durationMs   = domain.durationMs,
                detail       = domain.detail,
                occurredAt   = domain.occurredAt
            )
    }

    fun toDomain(): QuestionGenerationStepLog =
        QuestionGenerationStepLog(
            id = QuestionGenerationStepLogId(id),
            generationId = QuestionGenerationId(generationId),
            step         = step,
            status       = status,
            durationMs   = durationMs,
            detail       = detail,
            occurredAt   = occurredAt
        )
}
