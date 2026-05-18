package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainException

sealed class QuestionException(
    code: String,
    message: String,
) : DomainException(code, message) {
    class CannotModifyNonDraft(status: String) : QuestionException(
        "QUESTION_GROUP_CANNOT_MODIFY_NON_DRAFT",
        "DRAFT 상태가 아닌 그룹은 수정할 수 없습니다. 현재 상태: $status"
    )

    class QuestionAlreadyIn(questionId: Long) : QuestionException(
        "QUESTION_GROUP_QUESTION_ALREADY_EXISTS",
        "이미 그룹에 포함된 문제입니다. questionId: $questionId"
    )

    class StatusTransitionNotAllowed(from: String, to: String) : QuestionException(
        "QUESTION_GROUP_STATUS_TRANSITION_NOT_ALLOWED",
        "허용되지 않는 상태 전이입니다. $from → $to"
    )

    class CannotPublishEmpty : QuestionException(
        "QUESTION_GROUP_CANNOT_PUBLISH_EMPTY",
        "문제가 없는 그룹은 출제할 수 없습니다"
    )

    class NotFound(questionId: Long) : QuestionException(
        "QUESTION_NOT_FOUND",
        "questionId: $questionId 에 해당하는 문제를 찾을 수 없습니다."
    )
}
