package com.examhelper.api.token_usage.port.inbound.result

import com.examhelper.api.token_usage.port.inbound.view.TokenUsageDailyStatisticsView
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageStatisticsView

data class GetTokenUsageStatisticsResult(
    val summary: TokenUsageStatisticsView,
    val daily: List<TokenUsageDailyStatisticsView>,
    //val byModel: List<TokenUsageModelStatisticsView>,
)
