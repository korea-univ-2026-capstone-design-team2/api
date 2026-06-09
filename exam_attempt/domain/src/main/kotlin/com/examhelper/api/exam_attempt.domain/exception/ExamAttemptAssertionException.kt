package com.examhelper.api.exam_attempt.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class ExamAttemptAssertionException(
    code: String,
    message: String
) : DomainAssertionException(code, message) {
    class DuplicateQuestionItemIds : ExamAttemptAssertionException(
        code = "EXAM_ATTEMPT_DUPLICATE_QUESTION_ITEM_IDS",
        message = "동일한 questionItemId가 중복 저장되었습니다.",
    )

    class CannotModifySubmitted : ExamAttemptAssertionException(
        code = "EXAM_ATTEMPT_CANNOT_MODIFY_SUBMITTED",
        message = "제출된 응시는 수정할 수 없습니다.",
    )

    class AlreadySubmitted : ExamAttemptAssertionException(
        code = "EXAM_ATTEMPT_ALREADY_SUBMITTED",
        message = "이미 제출된 응시입니다.",
    )

    class NotSubmitted : ExamAttemptAssertionException(
        code = "EXAM_ATTEMPT_NOT_SUBMITTED",
        message = "아직 제출되지 않은 응시입니다.",
    )
}
