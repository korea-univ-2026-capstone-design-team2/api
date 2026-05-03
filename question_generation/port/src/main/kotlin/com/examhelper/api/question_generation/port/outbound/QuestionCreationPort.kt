package com.examhelper.api.question_generation.port.outbound

import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationCommand
import com.examhelper.api.question_generation.port.outbound.result.QuestionCreationResult

interface QuestionCreationPort {
    fun create(command: QuestionCreationCommand): QuestionCreationResult
}
