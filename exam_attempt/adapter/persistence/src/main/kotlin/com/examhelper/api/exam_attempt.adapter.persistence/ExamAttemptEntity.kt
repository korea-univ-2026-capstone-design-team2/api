package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.domain.ExamAttempt
import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import com.examhelper.api.kernel.identifier.ExamAttemptId
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "exam_attempts")
class ExamAttemptEntity(
    @Id
    val id: Long,

    @Column(nullable = false, updatable = false)
    val examId: Long,

    @Column(nullable = false, updatable = false)
    val memberId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ExamAttemptStatus,

    @OneToMany(
        mappedBy = "attempt",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    private val answers: MutableList<ExamAttemptAnswerEntity> = mutableListOf(),

    @Column(nullable = false, updatable = false)
    val startedAt: Instant,

    @Column
    val submittedAt: Instant?,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant,
) {

    companion object {
        fun fromDomain(
            domain: ExamAttempt,
        ): ExamAttemptEntity {
            val entity = ExamAttemptEntity(
                id = domain.id.value,
                examId = domain.examId.value,
                memberId = domain.memberId.value,
                status = domain.status,
                answers = mutableListOf(),
                startedAt = domain.startedAt,
                submittedAt = domain.submittedAt,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
            )

            val answerEntities = domain.answers.map {
                ExamAttemptAnswerEntity.fromDomain(
                    domain = it,
                    attempt = entity,
                )
            }

            entity.answers.addAll(answerEntities)

            return entity
        }
    }

    fun toDomain(): ExamAttempt =
        ExamAttempt.of(
            id = ExamAttemptId(id),
            examId = ExamId(examId),
            memberId = MemberId(memberId),
            answers = answers.map { it.toDomain() },
            status = status,
            startedAt = startedAt,
            submittedAt = submittedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
