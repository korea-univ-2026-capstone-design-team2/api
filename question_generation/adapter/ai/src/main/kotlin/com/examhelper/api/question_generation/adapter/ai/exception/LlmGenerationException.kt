package com.examhelper.api.question_generation.adapter.ai.exception

sealed class LlmGenerationException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
    sealed class Retryable(message: String, cause: Throwable? = null) : LlmGenerationException(message, cause) {
        class ApiCallFailed(cause: Throwable) : Retryable("LLM API 호출 실패", cause)
        class RateLimited(val retryAfterMillis: Long?) : Retryable("LLM API rate limit 초과")
        class ResponseParseFailed(cause: Throwable) : Retryable("LLM 응답 파싱 실패", cause)
        class EmptyResponse : Retryable("LLM 응답이 비어있음")
    }

    sealed class NonRetryable(message: String, cause: Throwable? = null) : LlmGenerationException(message, cause) {
        class InvalidPrompt(message: String) : NonRetryable(message)
        class ContentPolicyViolation(message: String) : NonRetryable(message)
    }
}
