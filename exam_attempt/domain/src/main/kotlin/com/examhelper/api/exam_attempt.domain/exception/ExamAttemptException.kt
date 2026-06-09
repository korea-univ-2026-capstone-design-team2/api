package com.examhelper.api.exam_attempt.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class ExamAttemptException(
    code: String,
    message: String,
    errorStatus: ErrorStatus,
) : DomainBusinessException(code, message, errorStatus) {
    class AlreadyInProgress(examId: Long, memberId: Long) : ExamAttemptException(
        code = "EXAM_ATTEMPT_ALREADY_IN_PROGRESS",
        message = "이미 진행중인 응시가 존재합니다. examId=$examId, memberId=$memberId",
        errorStatus = ErrorStatus.CONFLICT,
    )

    class ExamNotFound(examId: Long) : ExamAttemptException(
        code = "EXAM_NOT_FOUND",
        message = "examId: $examId 에 해당하는 시험을 찾을 수 없어 응시가 불가능합니다.",
        errorStatus = ErrorStatus.NOT_FOUND,
    )

    class NotFound(attemptId: Long) : ExamAttemptException(
        code = "EXAM_ATTEMPT_NOT_FOUND",
        message = "attemptId: $attemptId 에 해당하는 응시를 찾을 수 없습니다.",
        errorStatus = ErrorStatus.NOT_FOUND,
    )

    class Forbidden : ExamAttemptException(
        code = "EXAM_ATTEMPT_FORBIDDEN",
        message = "해당 응시에 접근할 수 없습니다.",
        errorStatus = ErrorStatus.FORBIDDEN,
    )
}
