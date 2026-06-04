package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.domain.entity.ExamAttemptAnswer
import com.examhelper.api.kernel.identifier.QuestionItemId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "exam_attempt_answers",
    uniqueConstraints = [UniqueConstraint(
            name = "uk_attempt_question_item",
            columnNames = ["attempt_id", "question_item_id"]
    )]
)
class ExamAttemptAnswerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "attempt_id",
        nullable = false,
    )
    val attempt: ExamAttemptEntity,

    @Column(nullable = false)
    val questionItemId: Long,

    @Column
    val selectedNumber: Int?,

    @Column(nullable = false)
    val timeSpentSeconds: Int,

    @Column(nullable = false)
    val markedUnknown: Boolean,

    @Column(nullable = false)
    val bookmarked: Boolean,
) {

    companion object {
        fun fromDomain(
            domain: ExamAttemptAnswer,
            attempt: ExamAttemptEntity,
        ): ExamAttemptAnswerEntity =
            ExamAttemptAnswerEntity(
                attempt = attempt,
                questionItemId = domain.questionItemId.value,
                selectedNumber = domain.selectedNumber,
                timeSpentSeconds = domain.timeSpentSeconds,
                markedUnknown = domain.markedUnknown,
                bookmarked = domain.bookmarked,
            )
    }

    fun toDomain(): ExamAttemptAnswer =
        ExamAttemptAnswer(
            questionItemId = QuestionItemId(questionItemId),
            selectedNumber = selectedNumber,
            timeSpentSeconds = timeSpentSeconds,
            markedUnknown = markedUnknown,
            bookmarked = bookmarked,
        )
}
