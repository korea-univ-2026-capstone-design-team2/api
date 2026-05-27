package com.examhelper.api.token_usage.port.inbound.view

import java.math.BigDecimal

data class TokenUsageStatisticsView(
    val totalRequests: Long,
    val totalPromptTokens: Long,
    val totalCompletionTokens: Long,
    val totalTokens: Long,
    val totalCost: BigDecimal
)
