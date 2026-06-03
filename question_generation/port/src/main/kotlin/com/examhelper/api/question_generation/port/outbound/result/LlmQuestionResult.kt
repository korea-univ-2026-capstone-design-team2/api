package com.examhelper.api.question_generation.port.outbound.result

data class LlmQuestionResult(
    val stem: String,
    val exhibit: LlmExhibitResult?,
    val choices: List<LlmChoiceResult>,
    val explanation: LlmExplanationResult
)
