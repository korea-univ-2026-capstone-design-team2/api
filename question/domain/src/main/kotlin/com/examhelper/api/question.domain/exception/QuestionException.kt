package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

class QuestionException {
    sealed class QuestionException(
        code: String,
        message: String,
        status: ErrorStatus,
    ) : DomainBusinessException(code, message, status) {

        class NotFound(id: String)
            : QuestionException("Q001", "문제를 찾을 수 없습니다: $id", ErrorStatus.NOT_FOUND)

        class AlreadyPublished(id: String)
            : QuestionException("Q002", "이미 출제된 문제입니다: $id", ErrorStatus.CONFLICT)

        class InvalidStatus(id: String)
            : QuestionException("Q003", "유효하지 않은 상태입니다: $id", ErrorStatus.BAD_REQUEST)
    }
}