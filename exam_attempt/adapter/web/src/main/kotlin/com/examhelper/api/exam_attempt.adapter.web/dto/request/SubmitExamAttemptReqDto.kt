package com.examhelper.api.exam_attempt.adapter.web.dto.request

import com.examhelper.api.exam_attempt.port.inbound.command.SubmitExamAttemptCommand
import com.examhelper.api.kernel.identifier.ExamAttemptId
import com.examhelper.api.kernel.identifier.MemberId
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "모의고사 제출 요청")
data class SubmitExamAttemptReqDto(
    val answers: List<SubmitExamAttemptAnswerReqDto>
) {
    fun toCommand(
        attemptId: String,
        memberId: String,
    ): SubmitExamAttemptCommand {
        return SubmitExamAttemptCommand(
            attemptId = ExamAttemptId(attemptId.toLong()),
            memberId = MemberId(memberId.toLong()),
            answers = answers.map { it.toCommand() },
        )
    }
}
