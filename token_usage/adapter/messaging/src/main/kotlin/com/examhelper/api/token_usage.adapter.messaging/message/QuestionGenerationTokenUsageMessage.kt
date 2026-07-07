package com.examhelper.api.token_usage.adapter.messaging.message

data class QuestionGenerationTokenUsageMessage(
    val generationId: Long,
    val memberId: Long,
    val model: String,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)
