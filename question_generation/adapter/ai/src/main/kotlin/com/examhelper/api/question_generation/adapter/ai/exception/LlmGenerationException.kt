package com.examhelper.api.question_generation.adapter.ai.exception

sealed class LlmGenerationException(
    val code    : String,
    message : String,
) : RuntimeException(message) {

    class ApiCallFailed(cause: Throwable) : LlmGenerationException(
        "LLM_API_CALL_FAILED",
        "LLM API 호출에 실패했습니다: ${cause.message}",
    ) {
        init { initCause(cause) }
    }

    class EmptyResponse : LlmGenerationException(
        "LLM_EMPTY_RESPONSE",
        "LLM이 빈 응답을 반환했습니다",
    )

    class ResponseParseFailed(cause: Throwable) : LlmGenerationException(
        "LLM_RESPONSE_PARSE_FAILED",
        "LLM 응답 JSON 파싱에 실패했습니다: ${cause.message}",
    ) {
        init { initCause(cause) }
    }

    class InvalidExhibit(reason: String) : LlmGenerationException(
        "LLM_INVALID_EXHIBIT",
        "LLM 응답의 보기 형식이 잘못됐습니다: $reason",
    )

    class InvalidChoice(reason: String) : LlmGenerationException(
        "LLM_INVALID_CHOICE",
        "LLM 응답의 선지 형식이 잘못됐습니다: $reason",
    )

    class InvalidExplanation(reason: String) : LlmGenerationException(
        "LLM_INVALID_EXPLANATION",
        "LLM 응답의 해설 형식이 잘못됐습니다: $reason",
    )
}
