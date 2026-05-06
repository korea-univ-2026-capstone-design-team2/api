package com.examhelper.api.question_generation.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class GenerationAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    // ── GenerationRequest ──────────────────────────────────────
    class QuantityOutOfRange(quantity: Int) : GenerationAssertionException(
        "GENERATION_QUANTITY_OUT_OF_RANGE",
        "생성 수량은 1 이상 20 이하여야 합니다: $quantity"
    )
    class TopKOutOfRange(topK: Int) : GenerationAssertionException(
        "GENERATION_TOP_K_OUT_OF_RANGE",
        "frameSearchTopK는 1 이상 10 이하여야 합니다: $topK"
    )
    class ReadingSubTypeRequired : GenerationAssertionException(
        "GENERATION_READING_SUB_TYPE_REQUIRED",
        "독해형 문제는 하위 유형이 필요합니다"
    )
    class SubTypeMismatch(questionType: String, subType: String) : GenerationAssertionException(
        "GENERATION_SUB_TYPE_MISMATCH",
        "$questionType 유형은 하위 유형을 가질 수 없습니다: $subType"
    )

    // ── GenerationTopic ────────────────────────────────────────
    class TopicCategoryBlank : GenerationAssertionException(
        "GENERATION_TOPIC_CATEGORY_BLANK",
        "주제 카테고리는 비어있을 수 없습니다"
    )
}
