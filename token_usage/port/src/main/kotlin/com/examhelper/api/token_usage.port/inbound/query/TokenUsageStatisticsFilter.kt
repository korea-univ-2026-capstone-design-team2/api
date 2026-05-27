package com.examhelper.api.token_usage.port.inbound.query

import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.type.TokenUsageStatus
import java.time.Instant

data class TokenUsageStatisticsFilter(
    val targetDomain: TokenUsageDomain? = null,
    val targetReferenceId: Long? = null,
    val provider: AiProvider? = null,
    val model: AiModel? = null,
    val status: TokenUsageStatus? = null,
    val from: Instant? = null,
    val to: Instant? = null
)
