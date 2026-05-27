package com.examhelper.api.token_usage.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import java.math.BigDecimal

sealed class TokenUsageAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class NegativePromptTokens(promptTokens: Int) : TokenUsageAssertionException(
        "TOKEN_USAGE_NEGATIVE_PROMPT_TOKENS",
        "promptTokens 는 음수일 수 없습니다. promptTokens=$promptTokens"
    )

    class NegativeCompletionTokens(completionTokens: Int) : TokenUsageAssertionException(
        "TOKEN_USAGE_NEGATIVE_COMPLETION_TOKENS",
        "completionTokens 는 음수일 수 없습니다. completionTokens=$completionTokens"
    )

    class InvalidTotalTokens(
        promptTokens: Int,
        completionTokens: Int,
        totalTokens: Int,
    ) : TokenUsageAssertionException(
        "TOKEN_USAGE_INVALID_TOTAL_TOKENS",
        "totalTokens 가 prompt+completion 과 일치하지 않습니다. " +
                "prompt=$promptTokens completion=$completionTokens total=$totalTokens"
    )

    class NegativePromptCost(promptCost: BigDecimal) : TokenUsageAssertionException(
        "TOKEN_USAGE_NEGATIVE_PROMPT_COST",
        "promptCost 는 음수일 수 없습니다. promptCost=$promptCost"
    )

    class NegativeCompletionCost(completionCost: BigDecimal) : TokenUsageAssertionException(
        "TOKEN_USAGE_NEGATIVE_COMPLETION_COST",
        "completionCost 는 음수일 수 없습니다. completionCost=$completionCost"
    )

    class InvalidTotalCost(
        promptCost: BigDecimal,
        completionCost: BigDecimal,
        totalCost: BigDecimal,
    ) : TokenUsageAssertionException(
        "TOKEN_USAGE_INVALID_TOTAL_COST",
        "totalCost 가 prompt+completion 과 일치하지 않습니다. " +
                "prompt=$promptCost completion=$completionCost total=$totalCost"
    )

    class ProviderMismatch(
        provider: AiProvider,
        model: AiModel,
    ) : TokenUsageAssertionException(
        "TOKEN_USAGE_PROVIDER_MISMATCH",
        "provider 와 model.provider 가 일치하지 않습니다. " +
                "provider=${provider.name}, model=${model.name}, modelProvider=${model.provider.name}"
    )
}
