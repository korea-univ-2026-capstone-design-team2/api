package com.examhelper.api.token_usage.port.inbound

import com.examhelper.api.token_usage.port.inbound.query.TokenUsageFilter
import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageListResult

interface GetTokenUsageListUseCase {
    fun execute(query: TokenUsageFilter): GetTokenUsageListResult
}
