package com.examhelper.api.token_usage.domain.vo

import com.examhelper.api.token_usage.domain.type.TokenUsageDomain

data class TokenUsageTarget(
    val domain: TokenUsageDomain,
    val referenceId: Long
)
