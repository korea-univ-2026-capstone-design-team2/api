package com.examhelper.api.exam_attempt.port.inbound.command

import com.examhelper.api.kernel.identifier.QuestionItemId

data class SaveExamAttemptAnswerCommand(
    val questionItemId: QuestionItemId,
    val selectedNumber: Int?,
    val timeSpentSeconds: Int,
    val markedUnknown: Boolean,
    val bookmarked: Boolean
)
