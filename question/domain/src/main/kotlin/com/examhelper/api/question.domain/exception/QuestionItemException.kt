package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.ErrorStatus

sealed class QuestionItemException(
    code: String,
    message: String,
    status: ErrorStatus,
) : DomainBusinessException(code, message, status) {
    class NotFound(id: String) : QuestionItemException(
        code = "QUESTION_ITEM_NOT_FOUND",
        message = "문제를 찾을 수 없습니다: $id",
        status = ErrorStatus.NOT_FOUND,
    )

    class QualityScoreTooLowToPublish(score: Double) : QuestionItemException(
        code = "QUESTION_ITEM_QUALITY_SCORE_TOO_LOW",
        message = "품질 점수가 출제 기준 미달입니다: $score",
        status = ErrorStatus.CONFLICT,
    )
}
