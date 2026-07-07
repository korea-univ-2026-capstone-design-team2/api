package com.examhelper.api.token_usage.port.inbound.command

import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.vo.TokenUsageTarget
import java.math.BigDecimal
import java.util.Currency

data class RecordTokenUsageCommand(
    val memberId: MemberId,
    val target: TokenUsageTarget,
    val model: AiModel,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)
