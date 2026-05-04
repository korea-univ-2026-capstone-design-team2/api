package com.examhelper.api.question_generation.port.outbound.result

data class LlmExplanationResult(
    val correctReason   : String,
    val incorrectReasons: Map<Int, String>,     // key: 선지 번호
)
