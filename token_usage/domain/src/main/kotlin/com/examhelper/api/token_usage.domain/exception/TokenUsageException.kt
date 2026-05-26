package com.examhelper.api.token_usage.domain.exception

import com.examhelper.api.kernel.core.exception.DomainException

sealed class TokenUsageException(
    code: String,
    message: String,
) : DomainException(code, message) {
    class AlreadyFailed : TokenUsageException(
        "TOKEN_USAGE_ALREADY_FAILED",
        "이미 실패 처리된 토큰 사용 기록입니다"
    )

    class NotFound(tokenUsageId: Long) : TokenUsageException(
        "TOKEN_USAGE_NOT_FOUND",
        "tokenUsageId: $tokenUsageId 에 해당하는 토큰 사용 기록을 찾을 수 없습니다."
    )
}
