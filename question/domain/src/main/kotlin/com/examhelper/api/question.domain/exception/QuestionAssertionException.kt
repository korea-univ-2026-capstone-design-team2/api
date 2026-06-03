package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class QuestionAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class DuplicateQuestionIds : QuestionAssertionException(
        "QUESTION_GROUP_DUPLICATE_QUESTION_IDS",
        "그룹 내 중복된 문제 ID가 존재합니다"
    )

    class SharedContextContentBlank : QuestionAssertionException(
        "QUESTION_GROUP_SHARED_CONTEXT_CONTENT_BLANK",
        "공유 지문 내용이 비어있습니다"
    )

    class MetadataSubjectBlank : QuestionAssertionException(
        "QUESTION_GROUP_METADATA_SUBJECT_BLANK",
        "그룹 메타데이터의 과목이 지정되지 않았습니다"
    )
}
