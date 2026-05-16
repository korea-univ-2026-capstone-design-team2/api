package com.examhelper.api.question_generation.port.outbound

import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult

interface LlmGenerationPort {
    suspend fun generate(command: LlmGenerationCommand): LlmGenerationResult
}
