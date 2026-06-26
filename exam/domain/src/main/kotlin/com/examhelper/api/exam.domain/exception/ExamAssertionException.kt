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

    class GenerationIdMismatch(
        expected: Long,
        actual: Long,
    ) : ExamAssertionException(
        code = "EXAM_GENERATION_ID_MISMATCH",
        message = "기대한 generationId와 다른 결과가 도착했습니다. expected=$expected, actual=$actual",
    )

    class StatusTransitionNotAllowed(
        from: String,
        to: String,
    ) : ExamAssertionException(
        code = "EXAM_STATUS_TRANSITION_NOT_ALLOWED",
        message = "허용되지 않는 상태 전이입니다. $from → $to"
    )

    class GenerationAlreadyStarted(generationId: Long) : ExamAssertionException(
        code = "EXAM_GENERATION_ALREADY_STARTED",
        message = "이미 generationId가 연결되어 있습니다. generationId=$generationId"
    )

    class CannotModifyItems(status: String) : ExamAssertionException(
        code = "EXAM_CANNOT_MODIFY_ITEMS",
        message = "문항 편입이 허용되지 않는 상태입니다. 현재 상태: $status"
    )

    class PendingGenerationResult : ExamAssertionException(
        code = "EXAM_PENDING_GENERATION_RESULT",
        message = "완료 처리에는 확정된 GenerationResult만 사용할 수 있습니다."
    )

    class InvalidImmediateCompletion : ExamAssertionException(
        code = "EXAM_INVALID_IMMEDIATE_COMPLETION",
        message = "즉시 완료는 빈 시험과 같이 비동기 문항 편입이 없는 경우에만 사용할 수 있습니다."
    )
}
