package com.examhelper.api.token_usage.adapter.web.dto.request

import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.vo.TokenUsageTarget
import com.examhelper.api.token_usage.port.inbound.command.RecordTokenUsageCommand
import java.math.BigDecimal
import java.util.Currency

data class RecordTokenUsageReqDto(
    val targetDomain: TokenUsageDomain,
    val targetReferenceId: Long,
    val provider: AiProvider,
    val model: AiModel,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val promptCost: BigDecimal,
    val completionCost: BigDecimal,
    val totalCost: BigDecimal,
    val currency: String
) {
    fun toCommand(): RecordTokenUsageCommand =
        RecordTokenUsageCommand(
            target = TokenUsageTarget(
                domain = targetDomain,
                referenceId = targetReferenceId,
            ),
            provider = provider,
            model = model,
            promptTokens = promptTokens,
            completionTokens = completionTokens,
            totalTokens = totalTokens,
            promptCost = promptCost,
            completionCost = completionCost,
            totalCost = totalCost,
            currency = Currency.getInstance(currency)
        )
}
