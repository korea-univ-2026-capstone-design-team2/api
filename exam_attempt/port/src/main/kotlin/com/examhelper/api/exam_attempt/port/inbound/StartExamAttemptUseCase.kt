package com.examhelper.api.exam_attempt.port.inbound

import com.examhelper.api.exam_attempt.port.inbound.command.StartExamAttemptCommand
import com.examhelper.api.exam_attempt.port.inbound.result.StartExamAttemptResult

interface StartExamAttemptUseCase {
    fun execute(command: StartExamAttemptCommand): StartExamAttemptResult
}
