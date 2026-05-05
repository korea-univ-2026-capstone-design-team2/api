package com.examhelper.api.question_generation.port.inbound

import com.examhelper.api.question_generation.port.inbound.command.GenerateQuestionCommand
import com.examhelper.api.question_generation.port.inbound.result.GenerateQuestionResult

interface GenerateQuestionUseCase {
    fun execute(command: GenerateQuestionCommand): GenerateQuestionResult
}
