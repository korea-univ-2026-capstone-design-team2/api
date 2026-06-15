package com.examhelper.api.exam_attempt.adapter.web.dto.request

import com.examhelper.api.exam_attempt.port.inbound.command.SaveExamAttemptAnswersCommand
import com.examhelper.api.kernel.identifier.ExamAttemptId
import com.examhelper.api.kernel.identifier.MemberId

data class SaveExamAttemptAnswersReqDto(
    val answers: List<SaveExamAttemptAnswerReqDto>
) {
    fun toCommand(attemptId: String, memberId: Long): SaveExamAttemptAnswersCommand {
        return SaveExamAttemptAnswersCommand(
            attemptId = ExamAttemptId(attemptId.toLong()),
            memberId = MemberId(memberId),
            answers = answers.map { it.toCommand() }
        )
    }
}
