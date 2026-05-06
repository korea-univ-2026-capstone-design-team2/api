package com.examhelper.api.question_generation.port.outbound.command

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult

data class QuestionCreationCommand(
    val result: LlmGenerationResult,
    val generationId: QuestionGenerationId,
    val metadata: QuestionCreationMetadata
)
