package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.domain.entity.ExamAttemptAnswer
import com.examhelper.api.kernel.identifier.QuestionItemId
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity
@Table(name = "exam_attempt_answers")
class ExamAttemptAnswerEntity(
    @EmbeddedId
    val id: ExamAttemptAnswerPk,

    @MapsId("attemptId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "attempt_id",
        nullable = false,
    )
    val attempt: ExamAttemptEntity,

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
                id = ExamAttemptAnswerPk(
                    attemptId = requireNotNull(attempt.id),
                    questionItemId = domain.questionItemId.value,
                ),
                attempt = attempt,
                selectedNumber = domain.selectedNumber,
                timeSpentSeconds = domain.timeSpentSeconds,
                markedUnknown = domain.markedUnknown,
                bookmarked = domain.bookmarked,
            )
    }

    fun toDomain(): ExamAttemptAnswer =
        ExamAttemptAnswer(
            questionItemId = QuestionItemId(id.questionItemId),
            selectedNumber = selectedNumber,
            timeSpentSeconds = timeSpentSeconds,
            markedUnknown = markedUnknown,
            bookmarked = bookmarked,
        )
}
