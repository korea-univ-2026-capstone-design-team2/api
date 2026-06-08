package com.examhelper.api.token_usage.adapter.web.dto.response

import com.examhelper.api.token_usage.port.inbound.view.TokenUsageStatisticsView
import java.math.BigDecimal

data class TokenUsageStatisticsResDto(
    val totalRequests: Long,
    val totalPromptTokens: Long,
    val totalCompletionTokens: Long,
    val totalTokens: Long,
    val totalCost: BigDecimal
) {
    companion object {
        fun from(view: TokenUsageStatisticsView): TokenUsageStatisticsResDto =
            TokenUsageStatisticsResDto(
                totalRequests = view.totalRequests,
                totalPromptTokens = view.totalPromptTokens,
                totalCompletionTokens = view.totalCompletionTokens,
                totalTokens = view.totalTokens,
                totalCost = view.totalCost
            )
    }
}
