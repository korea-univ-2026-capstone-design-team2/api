package com.examhelper.api.question.domain

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.exception.QuestionAssertionException
import java.time.Instant

/*
구조 설명을 위한 예시 입니다. 실제 구현은 추후 예정입니다.
 */
class Question(
    id: QuestionId,
    content: String,
    val createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now()
): AggregateRoot<QuestionId>(id) {
    init { validate() }

    var content: String = content
        private set
    var updatedAt: Instant = updatedAt
        private set

    companion object {
        // 새로운 Aggregate 객체 생성 시 사용
        fun create(
            id: QuestionId,
            content: String
        ): Question {
            return Question(id, content)
        }

        // DB에서 데이터를 조회하여 Aggregate 객체로 재조합할 때 사용.
        // createdAt, updatedAt 등 모든 필드를 DB에서 조회하여 명시적으로 받음
        fun of(
            id: QuestionId,
            content: String,
            createdAt: Instant,
            updatedAt: Instant
        ): Question {
            return Question(id, content, createdAt, updatedAt)
        }
    }

    private fun validate() {
        require(content.length <= 10000) { throw QuestionAssertionException.ContentTooLong(content.length) }
    }
}
