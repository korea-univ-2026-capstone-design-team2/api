package com.examhelper.api.learning_statistics.adapter.persistence.projection

import com.examhelper.api.kernel.identifier.DailyLearningStatId
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.learning_statistics.domain.DailyLearningStat
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
    name = "daily_learning_stats",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_daily_learning_stat",
            columnNames = [
                "member_id",
                "study_date",
                "subject"
            ]
        )
    ],
    indexes = [
        Index(
            name = "idx_daily_learning_member_date",
            columnList = "member_id, study_date"
        ),
        Index(
            name = "idx_daily_learning_member_subject",
            columnList = "member_id, subject"
        )
    ]
)
class DailyLearningStatEntity(
    @Id
    val id: Long,

    @Column(nullable = false, updatable = false)
    val memberId: Long,

    @Column(nullable = false, updatable = false)
    val studyDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val subject: Subject,

    @Column(nullable = false)
    val questionCount: Int,

    @Column(nullable = false)
    val correctCount: Int,

    @Column(nullable = false)
    val studySeconds: Long,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant
) {

    companion object {
        fun fromDomain(domain: DailyLearningStat): DailyLearningStatEntity =
            DailyLearningStatEntity(
                id = domain.id.value,
                memberId = domain.memberId.value,
                studyDate = domain.date,
                subject = domain.subject,
                questionCount = domain.questionCount,
                correctCount = domain.correctCount,
                studySeconds = domain.studySeconds,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
            )
    }

    fun toDomain(): DailyLearningStat =
        DailyLearningStat.of(
            id = DailyLearningStatId(id),
            memberId = MemberId(memberId),
            date = studyDate,
            subject = subject,
            questionCount = questionCount,
            correctCount = correctCount,
            studySeconds = studySeconds,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
