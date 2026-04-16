package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class QuestionAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class ContentTooLong(length: Int) : QuestionAssertionException(
        "QUESTION_CONTENT_TOO_LONG",
        "문제 내용이 너무 깁니다: $length/10000"
    )
}
