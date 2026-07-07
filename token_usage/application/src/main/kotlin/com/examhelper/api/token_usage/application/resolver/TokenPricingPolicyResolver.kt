package com.examhelper.api.token_usage.application.resolver

import com.examhelper.api.token_usage.domain.TokenPricingPolicy
import com.examhelper.api.token_usage.domain.type.AiProvider
import org.springframework.stereotype.Component

@Component
class TokenPricingPolicyResolver(
    policies: List<TokenPricingPolicy>,
) {
    private val policiesByProvider =
        policies.associateBy { policy ->
            AiProvider.entries.first(policy::supports)
        }

    fun resolve(provider: AiProvider): TokenPricingPolicy =
        policiesByProvider[provider]
            ?: throw IllegalArgumentException(
                "가격 정책이 존재하지 않습니다. provider=$provider"
            )
}
