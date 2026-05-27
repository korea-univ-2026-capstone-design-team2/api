package com.examhelper.api.token_usage.domain.vo

import com.examhelper.api.token_usage.domain.exception.TokenUsageAssertionException

data class TokenConsumption(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
) {
    private fun validate() {
        require(promptTokens >= 0) {
            throw TokenUsageAssertionException.NegativePromptTokens(promptTokens)
        }

        require(completionTokens >= 0) {
            throw TokenUsageAssertionException.NegativeCompletionTokens(completionTokens)
        }

        require(totalTokens == promptTokens + completionTokens) {
            throw TokenUsageAssertionException.InvalidTotalTokens(
                promptTokens = promptTokens,
                completionTokens = completionTokens,
                totalTokens = totalTokens
            )
        }
    }
}
