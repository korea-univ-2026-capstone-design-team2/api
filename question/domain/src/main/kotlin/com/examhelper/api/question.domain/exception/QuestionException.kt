package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class QuestionException(
    code: String,
    message: String,
    status: ErrorStatus,
) : DomainBusinessException(code, message, status) {
    class NotFound(id: String) : QuestionException(
        "Q001", "문제를 찾을 수 없습니다: $id", ErrorStatus.NOT_FOUND
    )
    class AlreadyPublished(id: String) : QuestionException(
        "Q002", "이미 출제된 문제입니다: $id", ErrorStatus.CONFLICT
    )
    class StatusTransitionNotAllowed(from: String, to: String) : QuestionException(
        "Q003", "상태 전이가 허용되지 않습니다: $from -> $to", ErrorStatus.BAD_REQUEST
    )
    class QualityScoreTooLowToPublish(score: Double) : QuestionException(
        "Q004", "품질 점수가 출제 기준 미달입니다: $score", ErrorStatus.BAD_REQUEST
    )
    class QualityScoreNotAssigned : QuestionException(
        "Q005", "품질 점수가 부여되지 않았습니다", ErrorStatus.BAD_REQUEST
    )
    class QuestionSetAlreadyAssigned(questionSetId: Long) : QuestionException(
        "Q006", "이미 세트에 편입된 문제입니다: set=$questionSetId", ErrorStatus.CONFLICT
    )
}
