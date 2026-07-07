package com.examhelper.api.token_usage.adapter.web.dto.request

import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.vo.TokenUsageTarget
import com.examhelper.api.token_usage.port.inbound.command.RecordTokenUsageCommand
import java.math.BigDecimal

data class RecordTokenUsageReqDto(
    val memberId: Long,
    val targetDomain: TokenUsageDomain,
    val targetReferenceId: Long,
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
            memberId = MemberId(memberId),
            target = TokenUsageTarget(
                domain = targetDomain,
                referenceId = targetReferenceId,
            ),
            model = model,
            promptTokens = promptTokens,
            completionTokens = completionTokens,
            totalTokens = totalTokens
        )
}
