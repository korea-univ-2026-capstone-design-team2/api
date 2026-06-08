package com.examhelper.api.token_usage.adapter.web.dto.response

import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.type.TokenUsageStatus
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageSummaryView
import java.math.BigDecimal
import java.time.Instant

data class TokenUsageSummaryResDto(
    val tokenUsageId: String,
    val targetDomain: TokenUsageDomain,
    val targetReferenceId: String,
    val provider: AiProvider,
    val model: AiModel,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val totalCost: BigDecimal,
    val currency: String,
    val status: TokenUsageStatus,
    val createdAt: Instant,
) {
    companion object {
        fun from(view: TokenUsageSummaryView): TokenUsageSummaryResDto =
            TokenUsageSummaryResDto(
                tokenUsageId = view.tokenUsageId.toString(),
                targetDomain = view.targetDomain,
                targetReferenceId = view.targetReferenceId.toString(),
                provider = view.provider,
                model = view.model,
                promptTokens = view.promptTokens,
                completionTokens = view.completionTokens,
                totalTokens = view.totalTokens,
                totalCost = view.totalCost,
                currency = view.currency,
                status = view.status,
                createdAt = view.createdAt
            )
    }
}
