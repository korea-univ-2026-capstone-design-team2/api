package com.examhelper.api.token_usage.port.inbound.view

import java.math.BigDecimal
import java.time.LocalDate

data class TokenUsageDailyStatisticsView(
    val date: LocalDate,
    val totalRequests: Long,
    val totalPromptTokens: Long,
    val totalCompletionTokens: Long,
    val totalTokens: Long,
    val totalCost: BigDecimal
)
