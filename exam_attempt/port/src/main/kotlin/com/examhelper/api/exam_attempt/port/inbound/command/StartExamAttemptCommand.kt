package com.examhelper.api.exam_attempt.port.inbound.command

import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId

data class StartExamAttemptCommand(
    val examId: ExamId,
    val memberId: MemberId
)
