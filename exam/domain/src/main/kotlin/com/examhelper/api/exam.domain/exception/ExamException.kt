package com.examhelper.api.exam.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class ExamException(
    code: String,
    message: String,
    status: ErrorStatus,
) : DomainBusinessException(code, message, status) {
    class NotFound(examId: Long) : ExamException(
        code = "EXAM_NOT_FOUND",
        message = "시험을 찾을 수 없습니다. examId: $examId",
        status = ErrorStatus.NOT_FOUND,
    )

    class NotFoundByGenerationId(generationId: Long) : ExamException(
        code = "EXAM_NOT_FOUND",
        message = "시험을 찾을 수 없습니다. generationId: $generationId",
        status = ErrorStatus.NOT_FOUND,
    )
}
