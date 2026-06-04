package com.examhelper.api.exam_attempt.port.inbound.command

data class SubmitExamAttemptAnswerCommand(
    val questionItemId: Long,
    val selectedNumber: Int?,
    val timeSpentSeconds: Int,
    val markedUnknown: Boolean,
    val bookmarked: Boolean
)
