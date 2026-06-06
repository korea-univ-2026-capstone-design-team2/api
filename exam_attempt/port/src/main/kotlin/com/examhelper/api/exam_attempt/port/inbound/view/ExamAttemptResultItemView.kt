package com.examhelper.api.exam_attempt.port.inbound.view

data class ExamAttemptResultItemView(
    val questionItemId: Long,
    val selectedNumber: Int?,
    val correctNumber: Int,
    val correct: Boolean,
    val timeSpentSeconds: Int
)
