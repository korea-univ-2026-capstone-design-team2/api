package com.examhelper.api.question_generation.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class GenerationException(
    code: String,
    message: String,
    status: ErrorStatus,
) : DomainBusinessException(code, message, status) {
    class NotFound(id: Long) : GenerationException(
        code = "GENERATION_NOT_FOUND",
        message = "생성 요청을 찾을 수 없습니다: $id",
        status = ErrorStatus.NOT_FOUND,
    )
}
