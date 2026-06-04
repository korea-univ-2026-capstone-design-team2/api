package com.examhelper.api.exam_attempt.adapter.web.dto.request

import com.examhelper.api.exam_attempt.port.inbound.command.SubmitExamAttemptAnswerCommand

data class SubmitExamAttemptAnswerReqDto(
    val questionItemId: Long,
    val selectedNumber: Int?,
    val timeSpentSeconds: Int,
    val markedUnknown: Boolean,
    val bookmarked: Boolean,
) {
    fun toCommand(): SubmitExamAttemptAnswerCommand {
        return SubmitExamAttemptAnswerCommand(
            questionItemId = questionItemId,
            selectedNumber = selectedNumber,
            timeSpentSeconds = timeSpentSeconds,
            markedUnknown = markedUnknown,
            bookmarked = bookmarked
        )
    }
}
