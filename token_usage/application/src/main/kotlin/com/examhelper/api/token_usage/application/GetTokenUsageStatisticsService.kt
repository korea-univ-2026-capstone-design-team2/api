package com.examhelper.api.token_usage.application

import com.examhelper.api.token_usage.port.inbound.GetTokenUsageStatisticsUseCase
import com.examhelper.api.token_usage.port.inbound.query.TokenUsageStatisticsFilter
import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageStatisticsResult
import com.examhelper.api.token_usage.port.outbound.TokenUsageReader
import org.springframework.stereotype.Service

@Service
class GetTokenUsageStatisticsService(
    private val tokenUsageReader: TokenUsageReader,
) : GetTokenUsageStatisticsUseCase {
    override fun execute(query: TokenUsageStatisticsFilter): GetTokenUsageStatisticsResult {
        val filter = TokenUsageStatisticsFilter(
            memberId = query.memberId,
            targetDomain = query.targetDomain,
            targetReferenceId = query.targetReferenceId,
            provider = query.provider,
            model = query.model,
            status = query.status,
            from = query.from,
            to = query.to
        )

        return GetTokenUsageStatisticsResult(
            summary = tokenUsageReader.findStatistics(filter),
            daily = tokenUsageReader.findDailyStatistics(filter),
            //byModel = tokenUsageReader.findModelStatistics(filter)
        )
    }
}
