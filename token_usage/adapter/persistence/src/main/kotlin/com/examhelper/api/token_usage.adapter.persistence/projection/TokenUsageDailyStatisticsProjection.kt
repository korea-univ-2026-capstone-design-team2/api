package com.examhelper.api.token_usage.adapter.persistence.projection

import java.math.BigDecimal
import java.time.LocalDate

interface TokenUsageDailyStatisticsProjection {
    val date: LocalDate
    val totalRequests: Long
    val totalPromptTokens: Long
    val totalCompletionTokens: Long
    val totalTokens: Long
    val totalCost: BigDecimal
}
