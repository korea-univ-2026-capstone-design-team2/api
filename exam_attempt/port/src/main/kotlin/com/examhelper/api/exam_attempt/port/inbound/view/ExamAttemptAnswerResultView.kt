package com.examhelper.api.exam_attempt.port.inbound.view

data class ExamAttemptAnswerResultView(
    val questionItemId: Long,
    val selectedNumber: Int?,
    val timeSpentSeconds: Int,
    val markedUnknown: Boolean,
    val bookmarked: Boolean
)
