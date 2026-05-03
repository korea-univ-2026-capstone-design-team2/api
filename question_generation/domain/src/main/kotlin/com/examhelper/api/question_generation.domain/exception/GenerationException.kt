package com.examhelper.api.question_generation.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class GenerationException(
    code: String,
    message: String,
    status: ErrorStatus,
) : DomainBusinessException(code, message, status) {

    class NotFound(id: Long) : GenerationException(
        "GEN001", "생성 요청을 찾을 수 없습니다: $id", ErrorStatus.NOT_FOUND
    )
    class AlreadyTerminated(id: Long, currentStatus: String) : GenerationException(
        "GEN002", "이미 종료된 생성 요청입니다: id=$id, status=$currentStatus", ErrorStatus.CONFLICT
    )
    class QuantityMismatch(expected: Int, actual: Int) : GenerationException(
        "GEN003", "생성된 문제 수가 요청과 다릅니다: expected=$expected, actual=$actual", ErrorStatus.INTERNAL_ERROR
    )
}
