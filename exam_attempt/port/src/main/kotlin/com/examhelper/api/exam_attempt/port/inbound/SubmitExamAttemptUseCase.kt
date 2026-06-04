package com.examhelper.api.exam_attempt.port.inbound

import com.examhelper.api.exam_attempt.port.inbound.command.SubmitExamAttemptCommand
import com.examhelper.api.exam_attempt.port.inbound.result.SubmitExamAttemptResult

interface SubmitExamAttemptUseCase {
    fun execute(command: SubmitExamAttemptCommand): SubmitExamAttemptResult
}
