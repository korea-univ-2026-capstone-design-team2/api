package com.examhelper.api.token_usage.port.inbound.result

import com.examhelper.api.token_usage.port.inbound.view.TokenUsageSummaryView

data class GetTokenUsageListResult(
    val items: List<TokenUsageSummaryView>,
    val totalCount: Long
)
