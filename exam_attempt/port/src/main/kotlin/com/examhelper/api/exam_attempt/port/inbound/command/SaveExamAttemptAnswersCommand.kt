package com.examhelper.api.exam_attempt.port.inbound.command

import com.examhelper.api.kernel.identifier.ExamAttemptId
import com.examhelper.api.kernel.identifier.MemberId

data class SaveExamAttemptAnswersCommand(
    val attemptId: ExamAttemptId,
    val memberId: MemberId,
    val answers: List<SaveExamAttemptAnswerCommand>
)
