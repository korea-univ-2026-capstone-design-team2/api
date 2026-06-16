package com.examhelper.api.token_usage.adapter.messaging.message

import java.math.BigDecimal

data class QuestionGenerationTokenUsageMessage(
    val generationId: Long,
    val memberId: Long,
    val provider: String,
    val model: String,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val promptCost: BigDecimal,
    val completionCost: BigDecimal,
    val totalCost: BigDecimal,
    val currency: String
)
