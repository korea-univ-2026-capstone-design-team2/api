package com.examhelper.api.exam_attempt.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class ExamAttemptAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class DuplicateQuestionItemIds : ExamAttemptAssertionException(
        "EXAM_ATTEMPT_DUPLICATE_QUESTION_ITEM_IDS",
        "동일한 questionItemId가 중복 저장되었습니다."
    )
}
