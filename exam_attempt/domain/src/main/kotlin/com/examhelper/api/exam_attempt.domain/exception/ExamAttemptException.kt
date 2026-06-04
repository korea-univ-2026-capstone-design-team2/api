package com.examhelper.api.exam_attempt.domain.exception

import com.examhelper.api.kernel.core.exception.DomainException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class ExamAttemptException(
    code: String,
    message: String,
    errorStatus: ErrorStatus
) : DomainException(code, message) {
    class AlreadyInProgress(
        examId: Long,
        memberId: Long,
    ) : ExamAttemptException(
        "EXAM_ATTEMPT_ALREADY_IN_PROGRESS",
        "이미 진행중인 응시가 존재합니다. examId=$examId, memberId=$memberId",
        ErrorStatus.CONFLICT,
    )

    class CannotModifySubmitted : ExamAttemptException(
        "EXAM_ATTEMPT_CANNOT_MODIFY_SUBMITTED",
        "제출된 응시는 수정할 수 없습니다.",
        ErrorStatus.CONFLICT,
    )

    class AlreadySubmitted : ExamAttemptException(
        "EXAM_ATTEMPT_ALREADY_SUBMITTED",
        "이미 제출된 응시입니다.",
        ErrorStatus.CONFLICT,
    )

    class ExamNotFound(
        examId: Long,
    ) : ExamAttemptException(
        "EXAM_NOT_FOUND",
        "examId: $examId 에 해당하는 시험을 찾을 수 없어 응시가 불가능합니다.",
        ErrorStatus.NOT_FOUND,
    )

    class NotFound(
        attemptId: Long,
    ) : ExamAttemptException(
        "EXAM_ATTEMPT_NOT_FOUND",
        "attemptId: $attemptId 에 해당하는 응시를 찾을 수 없습니다.",
        ErrorStatus.NOT_FOUND,
    )

    class Forbidden : ExamAttemptException(
        "EXAM_ATTEMPT_FORBIDDEN",
        "해당 응시에 접근할 수 없습니다.",
        ErrorStatus.FORBIDDEN,
    )

    class NotSubmitted : ExamAttemptException(
        "EXAM_ATTEMPT_NOT_SUBMITTED",
        "아직 제출되지 않은 응시입니다.",
        ErrorStatus.CONFLICT,
    )
}
