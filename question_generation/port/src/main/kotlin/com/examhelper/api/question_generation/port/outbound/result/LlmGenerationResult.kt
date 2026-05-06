package com.examhelper.api.question_generation.port.outbound.result

data class LlmGenerationResult(
    val stem        : String,
    val passage     : LlmPassageResult?,
    val exhibit     : LlmExhibitResult?,
    val choices     : List<LlmChoiceResult>,
    val explanation : LlmExplanationResult,
)
