package com.examhelper.api.question_generation.port.outbound.result

data class LlmTokenUsageResult(
    val model: String,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)
