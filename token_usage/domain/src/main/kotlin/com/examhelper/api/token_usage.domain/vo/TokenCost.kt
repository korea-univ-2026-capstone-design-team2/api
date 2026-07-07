package com.examhelper.api.token_usage.domain.vo

import com.examhelper.api.token_usage.domain.exception.TokenUsageAssertionException
import java.math.BigDecimal
import java.util.Currency

data class TokenCost(
    val promptCost: BigDecimal,
    val completionCost: BigDecimal,
    val totalCost: BigDecimal,
    val currency: Currency
) {
    init { validate() }

    private fun validate() {
        require(promptCost >= BigDecimal.ZERO) {
            throw TokenUsageAssertionException.NegativePromptCost(promptCost)
        }

        require(completionCost >= BigDecimal.ZERO) {
            throw TokenUsageAssertionException.NegativeCompletionCost(completionCost)
        }

        val expectedTotal = promptCost.add(completionCost)

        require(totalCost.compareTo(expectedTotal) == 0) {
            throw TokenUsageAssertionException.InvalidTotalCost(
                promptCost = promptCost,
                completionCost = completionCost,
                totalCost = totalCost,
            )
        }
    }
}
