package com.examhelper.api.token_usage.domain

import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.vo.TokenConsumption
import com.examhelper.api.token_usage.domain.vo.TokenCost

interface TokenPricingPolicy {
    fun supports(provider: AiProvider): Boolean

    fun calculate(
        model: AiModel,
        consumption: TokenConsumption,
    ): TokenCost
}
