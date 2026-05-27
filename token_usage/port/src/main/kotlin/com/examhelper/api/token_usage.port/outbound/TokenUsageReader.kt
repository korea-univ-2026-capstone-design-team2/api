package com.examhelper.api.token_usage.port.outbound

import com.examhelper.api.token_usage.port.inbound.query.TokenUsageFilter
import com.examhelper.api.token_usage.port.inbound.query.TokenUsageStatisticsFilter
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageDailyStatisticsView
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageStatisticsView
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageSummaryView

interface TokenUsageReader {
    fun findAll(filter: TokenUsageFilter): List<TokenUsageSummaryView>
    fun count(filter: TokenUsageFilter): Long
    fun findStatistics(filter: TokenUsageStatisticsFilter): TokenUsageStatisticsView
    fun findDailyStatistics(filter: TokenUsageStatisticsFilter): List<TokenUsageDailyStatisticsView>
}
