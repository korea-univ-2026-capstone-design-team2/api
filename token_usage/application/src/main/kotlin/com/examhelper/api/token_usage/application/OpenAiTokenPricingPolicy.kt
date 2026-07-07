package com.examhelper.api.token_usage.application

import com.examhelper.api.token_usage.domain.TokenPricingPolicy
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.vo.TokenConsumption
import com.examhelper.api.token_usage.domain.vo.TokenCost
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

@Component
class OpenAiTokenPricingPolicy : TokenPricingPolicy {
    override fun supports(provider: AiProvider): Boolean =
        provider == AiProvider.OPENAI

    override fun calculate(
        model: AiModel,
        consumption: TokenConsumption,
    ): TokenCost {
        val pricing = when (model) {
            AiModel.GPT_5_MINI ->
                Pricing(
                    promptPerMillion = BigDecimal("0.25"),
                    completionPerMillion = BigDecimal("2.00"),
                )

            AiModel.GPT_5_4_MINI ->
                Pricing(
                    promptPerMillion = BigDecimal("0.40"),
                    completionPerMillion = BigDecimal("3.20"),
                )

            else -> throw IllegalArgumentException(
                "지원하지 않는 OpenAI 모델입니다. model=$model"
            )
        }

        val promptCost = pricing.promptPerMillion
            .multiply(consumption.promptTokens.toBigDecimal())
            .divide(MILLION, SCALE, RoundingMode.HALF_UP)

        val completionCost = pricing.completionPerMillion
            .multiply(consumption.completionTokens.toBigDecimal())
            .divide(MILLION, SCALE, RoundingMode.HALF_UP)

        val totalCost = promptCost + completionCost

        return TokenCost(
            promptCost = promptCost,
            completionCost = completionCost,
            totalCost = totalCost,
            currency = USD,
        )
    }

    private data class Pricing(
        val promptPerMillion: BigDecimal,
        val completionPerMillion: BigDecimal,
    )

    companion object {
        private val MILLION = BigDecimal("1000000")
        private const val SCALE = 8
        private val USD = Currency.getInstance("USD")
    }
}
