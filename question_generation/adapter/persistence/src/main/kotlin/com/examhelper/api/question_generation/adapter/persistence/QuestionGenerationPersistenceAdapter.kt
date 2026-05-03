package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.question_generation.domain.QuestionGeneration
import com.examhelper.api.question_generation.port.outbound.QuestionGenerationStore
import org.springframework.stereotype.Repository

@Repository
class QuestionGenerationPersistenceAdapter(
    private val jpaStore: QuestionGenerationJpaStore
): QuestionGenerationStore {
    override fun save(questionGeneration: QuestionGeneration) {
        val entity = QuestionGenerationEntity.fromDomain(questionGeneration)
        jpaStore.save(entity)
    }
}
