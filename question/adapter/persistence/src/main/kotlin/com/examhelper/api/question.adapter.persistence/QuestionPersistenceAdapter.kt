package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.port.outbound.QuestionStore
import org.springframework.stereotype.Repository

@Repository
class QuestionPersistenceAdapter(
    private val jpaStore: QuestionJpaStore
): QuestionStore {
    override fun save(question: Question) {
        val entity = QuestionEntity.fromDomain(question)
        jpaStore.save(entity)
    }

    override fun saveAll(questions: List<Question>) {
        val entities = questions.map(QuestionEntity::fromDomain)
        jpaStore.saveAll(entities)
    }

    override fun loadById(id: QuestionId): Question? {
        return jpaStore.findById(id.value).orElse(null)?.toDomain()
    }
}
