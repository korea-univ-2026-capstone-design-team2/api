package com.examhelper.api.exam.domain.exception

import com.examhelper.api.kernel.core.exception.DomainException

sealed class ExamException(
    code: String,
    message: String,
) : DomainException(code, message) {
    class StatusTransitionNotAllowed(from: String, to: String) : ExamException(
        code = "EXAM_STATUS_TRANSITION_NOT_ALLOWED",
        message = "허용되지 않는 상태 전이입니다. $from → $to",
    )

    class CannotModifyItems(status: String) : ExamException(
        code = "EXAM_CANNOT_MODIFY_ITEMS",
        message = "GENERATING 상태가 아닐 때는 문항을 편입할 수 없습니다. 현재 상태: $status",
    )

    class ItemAlreadyExists(itemId: Long) : ExamException(
        code = "EXAM_ITEM_ALREADY_EXISTS",
        message = "이미 편입된 문항입니다. examItemId: $itemId",
    )

    class NotFound(examId: Long) : ExamException(
        code = "EXAM_NOT_FOUND",
        message = "시험을 찾을 수 없습니다. examId: $examId",
    )
}
