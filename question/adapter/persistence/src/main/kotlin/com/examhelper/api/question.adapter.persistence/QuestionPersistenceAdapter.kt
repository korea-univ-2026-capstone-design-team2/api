package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.port.outbound.QuestionRepository
import org.springframework.stereotype.Repository

/**
 * QuestionPersistenceAdapter는 QuestionRepository 인터페이스를 구현합니다.
 * QuestionJpaRepository를 주입받아 사용하여 실제 데이터베이스 작업을 수행합니다.
 */
@Repository
class QuestionPersistenceAdapter(
    private val jpaRepository: QuestionJpaRepository
): QuestionRepository {
    override fun save(question: Question) {
        val questionEntity = QuestionEntity.fromDomain(question)
        jpaRepository.save(questionEntity)
    }
}
