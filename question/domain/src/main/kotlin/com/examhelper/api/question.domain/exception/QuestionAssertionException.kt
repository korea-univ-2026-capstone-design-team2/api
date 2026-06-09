package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class QuestionAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {

    class DuplicateQuestionIds : QuestionAssertionException(
        code = "QUESTION_GROUP_DUPLICATE_QUESTION_IDS",
        message = "그룹 내 중복된 문제 ID가 존재합니다",
    )

    class SharedContextContentBlank : QuestionAssertionException(
        code = "QUESTION_GROUP_SHARED_CONTEXT_CONTENT_BLANK",
        message = "공유 지문 내용이 비어있습니다",
    )

    class MetadataSubjectBlank : QuestionAssertionException(
        code = "QUESTION_GROUP_METADATA_SUBJECT_BLANK",
        message = "그룹 메타데이터의 과목이 지정되지 않았습니다",
    )

    class CannotModifyNonDraft(status: String) : QuestionAssertionException(
        code = "QUESTION_GROUP_CANNOT_MODIFY_NON_DRAFT",
        message = "DRAFT 상태가 아닌 그룹은 수정할 수 없습니다. 현재 상태: $status",
    )

    class QuestionAlreadyIn(questionItemId: Long) : QuestionAssertionException(
        code = "QUESTION_GROUP_QUESTION_ALREADY_EXISTS",
        message = "이미 그룹에 포함된 문제입니다. questionId: $questionItemId",
    )

    class StatusTransitionNotAllowed(from: String, to: String) : QuestionAssertionException(
        code = "QUESTION_GROUP_STATUS_TRANSITION_NOT_ALLOWED",
        message = "허용되지 않는 상태 전이입니다. $from → $to",
    )

    class CannotPublishEmpty : QuestionAssertionException(
        code = "QUESTION_GROUP_CANNOT_PUBLISH_EMPTY",
        message = "문제가 없는 그룹은 출제할 수 없습니다",
    )
}
