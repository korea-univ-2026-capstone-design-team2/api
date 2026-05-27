package com.examhelper.api.token_usage.port.inbound.view

import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.type.TokenUsageStatus
import java.math.BigDecimal
import java.time.Instant

data class TokenUsageSummaryView(
    val tokenUsageId: Long,
    val targetDomain: TokenUsageDomain,
    val targetReferenceId: Long,
    val provider: AiProvider,
    val model: AiModel,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val totalCost: BigDecimal,
    val currency: String,
    val status: TokenUsageStatus,
    val createdAt: Instant
)
