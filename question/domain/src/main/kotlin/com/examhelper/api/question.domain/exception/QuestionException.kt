package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class QuestionException(
    code: String,
    message: String,
    status: ErrorStatus
) : DomainBusinessException(code, message, status) {

    class NotFound(questionId: Long) : QuestionException(
        code = "QUESTION_NOT_FOUND",
        message = "questionId: $questionId 에 해당하는 문제를 찾을 수 없습니다.",
        status = ErrorStatus.NOT_FOUND
    )
}
