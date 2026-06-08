package com.examhelper.api.token_usage.adapter.web.dto.response

import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageStatisticsResult

data class GetTokenUsageStatisticsResDto(
    val summary: TokenUsageStatisticsResDto,
    val daily: List<TokenUsageDailyStatisticsResDto>,
) {
    companion object {
        fun from(result: GetTokenUsageStatisticsResult): GetTokenUsageStatisticsResDto =
            GetTokenUsageStatisticsResDto(
                summary = TokenUsageStatisticsResDto.from(result.summary),
                daily = result.daily.map(TokenUsageDailyStatisticsResDto::from)
            )
    }
}
