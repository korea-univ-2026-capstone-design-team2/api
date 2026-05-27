package com.examhelper.api.token_usage.port.inbound

import com.examhelper.api.token_usage.port.inbound.query.TokenUsageStatisticsFilter
import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageStatisticsResult

interface GetTokenUsageStatisticsUseCase {
    fun execute(query: TokenUsageStatisticsFilter): GetTokenUsageStatisticsResult
}
