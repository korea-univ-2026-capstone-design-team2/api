package com.examhelper.api.token_usage.adapter.persistence

import com.examhelper.api.token_usage.port.inbound.query.TokenUsageFilter
import com.examhelper.api.token_usage.port.inbound.query.TokenUsageStatisticsFilter
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageDailyStatisticsView
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageStatisticsView
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageSummaryView
import com.examhelper.api.token_usage.port.outbound.TokenUsageReader
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class TokenUsageQueryAdapter(
    private val tokenUsageJpaReader: TokenUsageJpaReader,
) : TokenUsageReader {

    // ── Raw Usage ───────────────────────────────────────
    override fun findAll(filter: TokenUsageFilter): List<TokenUsageSummaryView> =
        tokenUsageJpaReader.findSummaries(
            targetDomain = filter.targetDomain,
            targetReferenceId = filter.targetReferenceId,
            provider = filter.provider,
            model = filter.model,
            status = filter.status,
            from = filter.from,
            to = filter.to,
            pageable = PageRequest.of(filter.page, filter.size)
        )

    override fun count(filter: TokenUsageFilter): Long =
        tokenUsageJpaReader.countByFilter(
            targetDomain = filter.targetDomain,
            targetReferenceId = filter.targetReferenceId,
            provider = filter.provider,
            model = filter.model,
            status = filter.status,
            from = filter.from,
            to = filter.to,
        )

    // ── Statistics ──────────────────────────────────────
    override fun findStatistics(filter: TokenUsageStatisticsFilter): TokenUsageStatisticsView {
        val projection = tokenUsageJpaReader.findStatistics(
            targetDomain = filter.targetDomain,
            targetReferenceId = filter.targetReferenceId,
            provider = filter.provider,
            model = filter.model,
            status = filter.status,
            from = filter.from,
            to = filter.to
        )

        return TokenUsageStatisticsView(
            totalRequests = projection.totalRequests,
            totalPromptTokens = projection.totalPromptTokens,
            totalCompletionTokens = projection.totalCompletionTokens,
            totalTokens = projection.totalTokens,
            totalCost = projection.totalCost
        )
    }

    override fun findDailyStatistics(
        filter: TokenUsageStatisticsFilter,
    ): List<TokenUsageDailyStatisticsView> {
        val projections = tokenUsageJpaReader.findDailyStatistics(
            targetDomain = filter.targetDomain,
            targetReferenceId = filter.targetReferenceId,
            provider = filter.provider,
            model = filter.model,
            status = filter.status,
            from = filter.from,
            to = filter.to,
        )

        return projections.map { TokenUsageDailyStatisticsView(
            date = it.date,
            totalRequests = it.totalRequests,
            totalPromptTokens = it.totalPromptTokens,
            totalCompletionTokens = it.totalCompletionTokens,
            totalTokens = it.totalTokens,
            totalCost = it.totalCost
        ) }
    }

    /*
    override fun findModelStatistics(
        filter: TokenUsageStatisticsFilter,
    ): List<TokenUsageModelStatisticsView> =
        tokenUsageJpaReader.findModelStatistics(
            targetDomain = filter.targetDomain,
            targetReferenceId = filter.targetReferenceId,
            provider = filter.provider,
            model = filter.model,
            status = filter.status,
            from = filter.from,
            to = filter.to,
        )

     */
}
