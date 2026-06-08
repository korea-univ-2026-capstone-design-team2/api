package com.examhelper.api.token_usage.adapter.web.dto.response

import com.examhelper.api.token_usage.port.inbound.view.TokenUsageDailyStatisticsView
import java.math.BigDecimal
import java.time.LocalDate

data class TokenUsageDailyStatisticsResDto(
    val date: LocalDate,
    val totalRequests: Long,
    val totalPromptTokens: Long,
    val totalCompletionTokens: Long,
    val totalTokens: Long,
    val totalCost: BigDecimal,
) {
    companion object {
        fun from(view: TokenUsageDailyStatisticsView): TokenUsageDailyStatisticsResDto =
            TokenUsageDailyStatisticsResDto(
                date = view.date,
                totalRequests = view.totalRequests,
                totalPromptTokens = view.totalPromptTokens,
                totalCompletionTokens = view.totalCompletionTokens,
                totalTokens = view.totalTokens,
                totalCost = view.totalCost
            )
    }
}
