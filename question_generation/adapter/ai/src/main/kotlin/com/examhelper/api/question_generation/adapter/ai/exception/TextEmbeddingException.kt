package com.examhelper.api.question_generation.adapter.ai.exception

sealed class TextEmbeddingException(
    val code: String,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    class EmbeddingFailed(cause: Throwable) : TextEmbeddingException(
        "FRAME_SEARCH_EMBEDDING_FAILED",
        "텍스트 임베딩에 실패했습니다",
        cause
    )
}
