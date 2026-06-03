package com.examhelper.api.token_usage.adapter.persistence.projection

import java.math.BigDecimal

interface TokenUsageStatisticsProjection {
    val totalRequests: Long
    val totalPromptTokens: Long
    val totalCompletionTokens: Long
    val totalTokens: Long
    val totalCost: BigDecimal
}
