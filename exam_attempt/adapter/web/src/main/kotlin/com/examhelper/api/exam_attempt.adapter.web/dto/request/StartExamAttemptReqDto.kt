package com.examhelper.api.exam_attempt.adapter.web.dto.request

import com.examhelper.api.exam_attempt.port.inbound.command.StartExamAttemptCommand
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId

data class StartExamAttemptReqDto(
    val examId: String
) {
    fun toCommand(): StartExamAttemptCommand =
        StartExamAttemptCommand(
            examId = ExamId(examId.toLong()),
            memberId = MemberId(1L)
        )
}
