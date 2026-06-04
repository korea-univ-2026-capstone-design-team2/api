package com.examhelper.api.exam_attempt.adapter.web.dto.request

import com.examhelper.api.exam_attempt.port.inbound.command.SaveExamAttemptAnswerCommand
import com.examhelper.api.kernel.identifier.QuestionItemId

data class SaveExamAttemptAnswerReqDto(
    val questionItemId: Long,
    val selectedNumber: Int?,
    val timeSpentSeconds: Int,
    val markedUnknown: Boolean,
    val bookmarked: Boolean
) {
    fun toCommand(): SaveExamAttemptAnswerCommand {
        return SaveExamAttemptAnswerCommand(
            questionItemId = QuestionItemId(questionItemId),
            selectedNumber = selectedNumber,
            timeSpentSeconds = timeSpentSeconds,
            markedUnknown = markedUnknown,
            bookmarked = bookmarked
        )
    }
}
