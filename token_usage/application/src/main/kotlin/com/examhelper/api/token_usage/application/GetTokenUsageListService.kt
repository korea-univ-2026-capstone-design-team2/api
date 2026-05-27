package com.examhelper.api.token_usage.application

import com.examhelper.api.token_usage.port.inbound.GetTokenUsageListUseCase
import com.examhelper.api.token_usage.port.inbound.query.TokenUsageFilter
import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageListResult
import com.examhelper.api.token_usage.port.outbound.TokenUsageReader
import org.springframework.stereotype.Service

@Service
class GetTokenUsageListService(
    private val tokenUsageReader: TokenUsageReader
) : GetTokenUsageListUseCase {
    override fun execute(query: TokenUsageFilter): GetTokenUsageListResult {
        return GetTokenUsageListResult(
            items = tokenUsageReader.findAll(query),
            totalCount = tokenUsageReader.count(query)
        )
    }
}
