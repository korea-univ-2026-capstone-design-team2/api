package com.examhelper.api.exam_attempt.adapter.persistence

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ExamAttemptAnswerPk(
    @Column(name = "attempt_id")
    val attemptId: Long,

    @Column(name = "question_item_id")
    val questionItemId: Long,
)
