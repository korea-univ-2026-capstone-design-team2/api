package com.examhelper.api.question_generation.adapter.persistence.exception

sealed class FrameSearchException(
    val code: String,
    message: String,
) : RuntimeException(message) {
    class PayloadFieldMissing(field: String) : FrameSearchException(
        "FRAME_SEARCH_PAYLOAD_FIELD_MISSING",
        "Qdrant payload에 필수 필드가 없습니다: $field",
    )

    class QdrantUnavailable(
        collection: String,
        cause: Throwable,
    ) : FrameSearchException(
        "FRAME_SEARCH_QDRANT_UNAVAILABLE",
        "Qdrant 컬렉션에 접근할 수 없습니다: $collection",
    )
}