package com.examhelper.api.question_generation.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class GenerationAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {

    // ── GenerationRequest ──────────────────────────────────────
    class QuantityOutOfRange(quantity: Int) : GenerationAssertionException(
        code = "GENERATION_QUANTITY_OUT_OF_RANGE",
        message = "생성 수량은 1 이상 20 이하여야 합니다: $quantity",
    )

    class TopKOutOfRange(topK: Int) : GenerationAssertionException(
        code = "GENERATION_TOP_K_OUT_OF_RANGE",
        message = "frameSearchTopK는 1 이상 10 이하여야 합니다: $topK",
    )

    class ReadingSubTypeRequired : GenerationAssertionException(
        code = "GENERATION_READING_SUB_TYPE_REQUIRED",
        message = "독해형 문제는 하위 유형이 필요합니다",
    )

    class SubTypeMismatch(questionType: String, subType: String) : GenerationAssertionException(
        code = "GENERATION_SUB_TYPE_MISMATCH",
        message = "$questionType 유형은 하위 유형을 가질 수 없습니다: $subType",
    )

    // ── GenerationTopic ────────────────────────────────────────
    class TopicCategoryBlank : GenerationAssertionException(
        code = "GENERATION_TOPIC_CATEGORY_BLANK",
        message = "주제 카테고리는 비어있을 수 없습니다",
    )

    // ── GenerationStatus ───────────────────────────────────────
    class AlreadyTerminated(id: Long, currentStatus: String) : GenerationAssertionException(
        code = "GENERATION_ALREADY_TERMINATED",
        message = "이미 종료된 생성 요청입니다: id=$id, status=$currentStatus",
    )

    class QuantityMismatch(expected: Int, actual: Int) : GenerationAssertionException(
        code = "GENERATION_QUANTITY_MISMATCH",
        message = "생성된 문제 수가 요청과 다릅니다: expected=$expected, actual=$actual",
    )
}
