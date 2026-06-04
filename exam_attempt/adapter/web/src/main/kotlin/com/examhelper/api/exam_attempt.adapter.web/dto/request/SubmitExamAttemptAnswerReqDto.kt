package com.examhelper.api.exam_attempt.adapter.web.dto.request

import com.examhelper.api.exam_attempt.port.inbound.command.SubmitExamAttemptAnswerCommand
import com.examhelper.api.kernel.identifier.QuestionItemId

data class SubmitExamAttemptAnswerReqDto(
    val questionItemId: Long,
    val selectedNumber: Int?,
    val timeSpentSeconds: Int,
    val markedUnknown: Boolean,
    val bookmarked: Boolean,
) {
    fun toCommand(): SubmitExamAttemptAnswerCommand {
        return SubmitExamAttemptAnswerCommand(
            questionItemId = QuestionItemId(questionItemId),
            selectedNumber = selectedNumber,
            timeSpentSeconds = timeSpentSeconds,
            markedUnknown = markedUnknown,
            bookmarked = bookmarked
        )
    }
}
