package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.port.outbound.QuestionStore
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

/**
 * QuestionPersistenceAdapter는 QuestionRepository 인터페이스를 구현합니다.
 * QuestionJpaRepository를 주입받아 사용하여 실제 데이터베이스 작업을 수행합니다.
 */
@Repository
class QuestionPersistenceAdapter(
    private val jpaStore: QuestionJpaStore
): QuestionStore {
    override fun save(question: Question) {
        val questionEntity = QuestionEntity.fromDomain(question)
        jpaStore.save(questionEntity)
    }

    override fun loadById(id: QuestionId): Question? {
        return jpaStore.findByIdOrNull(id.value)?.toDomain()
    }
}
