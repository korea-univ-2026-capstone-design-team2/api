package com.examhelper.api.question_generation.port.outbound.result

data class LlmGenerationResult(
    val sharedContext: LlmSharedContextResult?,
    val questions: List<LlmQuestionResult>,
    val usage: LlmTokenUsageResult
)
