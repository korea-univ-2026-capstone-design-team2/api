package com.examhelper.api.exam_attempt.port.inbound

import com.examhelper.api.exam_attempt.port.inbound.command.SaveExamAttemptAnswersCommand
import com.examhelper.api.exam_attempt.port.inbound.result.SaveExamAttemptAnswersResult

interface SaveExamAttemptAnswersUseCase {
    fun execute(command: SaveExamAttemptAnswersCommand): SaveExamAttemptAnswersResult
}
