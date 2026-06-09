package com.examhelper.api.exam.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException
import com.examhelper.api.kernel.core.exception.ErrorStatus

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

    class StatusTransitionNotAllowed(from: String, to: String) : ExamException(
        code = "EXAM_STATUS_TRANSITION_NOT_ALLOWED",
        message = "허용되지 않는 상태 전이입니다. $from → $to",
        status = ErrorStatus.CONFLICT,
    )

    class CannotModifyItems(status: String) : ExamException(
        code = "EXAM_CANNOT_MODIFY_ITEMS",
        message = "GENERATING 상태가 아닐 때는 문항을 편입할 수 없습니다. 현재 상태: $status",
        status = ErrorStatus.CONFLICT,
    )

    class ItemAlreadyExists(itemId: Long) : ExamException(
        code = "EXAM_ITEM_ALREADY_EXISTS",
        message = "이미 편입된 문항입니다. examItemId: $itemId",
        status = ErrorStatus.CONFLICT,
    )
}
