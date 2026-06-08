package com.examhelper.api.token_usage.adapter.web.dto.response

import com.examhelper.api.token_usage.port.inbound.result.GetTokenUsageListResult

data class GetTokenUsageListResDto(
    val items: List<TokenUsageSummaryResDto>,
    val totalCount: Long,
) {
    companion object {
        fun fromResult(result: GetTokenUsageListResult): GetTokenUsageListResDto =
            GetTokenUsageListResDto(
                items = result.items.map(TokenUsageSummaryResDto::from),
                totalCount = result.totalCount
            )
    }
}
