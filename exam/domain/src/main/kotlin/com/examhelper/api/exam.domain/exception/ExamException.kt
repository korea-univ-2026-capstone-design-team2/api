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
        message = "시험을 찾을 수 없습니다. examId=$examId",
        status = ErrorStatus.NOT_FOUND,
    )

    class NotFoundByGenerationId(generationId: Long) : ExamException(
        code = "EXAM_NOT_FOUND_BY_GENERATION_ID",
        message = "generationId에 해당하는 시험을 찾을 수 없습니다. generationId=$generationId",
        status = ErrorStatus.NOT_FOUND,
    )

    class ItemAlreadyExists(questionId: Long) : ExamException(
        code = "EXAM_ITEM_ALREADY_EXISTS",
        message = "이미 편입된 문항입니다. questionId=$questionId",
        status = ErrorStatus.CONFLICT,
    )
}
