package com.examhelper.api.exam.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class ExamAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class DuplicateItemIds : ExamAssertionException(
        code = "EXAM_DUPLICATE_ITEM_IDS",
        message = "시험 내 중복된 문항 ID가 존재합니다.",
    )

    class EmptyItemsOnComplete : ExamAssertionException(
        code = "EXAM_EMPTY_ITEMS_ON_COMPLETE",
        message = "생성 완료 처리 시 편입된 문항이 없습니다.",
    )
}
