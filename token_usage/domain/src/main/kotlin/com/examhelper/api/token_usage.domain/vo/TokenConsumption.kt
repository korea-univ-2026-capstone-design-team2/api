package com.examhelper.api.token_usage.domain.vo

data class TokenConsumption(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)
