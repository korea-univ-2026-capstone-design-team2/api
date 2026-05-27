package com.examhelper.api.token_usage.port.inbound.command

import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.vo.TokenUsageTarget
import java.math.BigDecimal
import java.util.Currency

data class RecordTokenUsageCommand(
    val target: TokenUsageTarget,
    val provider: AiProvider,
    val model: AiModel,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val promptCost: BigDecimal,
    val completionCost: BigDecimal,
    val totalCost: BigDecimal,
    val currency: Currency
)
