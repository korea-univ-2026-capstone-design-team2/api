package com.examhelper.api.token_usage.domain.vo

import java.math.BigDecimal
import java.util.Currency

data class TokenCost(
    val promptCost: BigDecimal,
    val completionCost: BigDecimal,
    val totalCost: BigDecimal,
    val currency: Currency
)
