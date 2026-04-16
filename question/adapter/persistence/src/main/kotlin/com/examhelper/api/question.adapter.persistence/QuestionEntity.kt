package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.Question
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant


/**
MySql DB 스키마를 JPA Entity를 통해 정의합니다.
fromDomain(): Question 도메인 객체를 QuestionEntity로 변환하는 팩토리 메서드입니다.
toDomain(): QuestionEntity 객체를 Question 도메인 객체로 변환하는 메서드입니다.
*/
@Entity
@Table(name = "questions")
class QuestionEntity (
    @Id
    val id: Long,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant
) {
    companion object {
        fun fromDomain(domain: Question): QuestionEntity {
            return QuestionEntity(
                id = domain.id.value,
                content = domain.content,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt
            )
        }
    }

    fun toDomain(): Question {
        return Question.of(
            id = QuestionId(id),
            content = content,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
