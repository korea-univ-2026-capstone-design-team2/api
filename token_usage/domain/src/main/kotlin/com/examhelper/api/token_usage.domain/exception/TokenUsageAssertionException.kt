package com.examhelper.api.token_usage.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class TokenUsageAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class InvalidTotalTokens : TokenUsageAssertionException(
        "TOKEN_USAGE_INVALID_TOTAL_TOKENS",
        "전체 토큰 수가 prompt/completion 토큰 합과 일치하지 않습니다"
    )

    class InvalidTotalCost : TokenUsageAssertionException(
        "TOKEN_USAGE_INVALID_TOTAL_COST",
        "전체 비용이 prompt/completion 비용 합과 일치하지 않습니다"
    )

    class NegativeTokenCount : TokenUsageAssertionException(
        "TOKEN_USAGE_NEGATIVE_TOKEN_COUNT",
        "토큰 수는 음수가 될 수 없습니다"
    )

    class NegativeCost : TokenUsageAssertionException(
        "TOKEN_USAGE_NEGATIVE_COST",
        "비용은 음수가 될 수 없습니다"
    )
}
