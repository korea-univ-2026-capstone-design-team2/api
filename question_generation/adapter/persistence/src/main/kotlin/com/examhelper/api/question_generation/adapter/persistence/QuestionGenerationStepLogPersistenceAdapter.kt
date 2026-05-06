package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.question_generation.port.inbound.model.QuestionGenerationStepLog
import com.examhelper.api.question_generation.port.outbound.QuestionGenerationStepLogStore
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
class QuestionGenerationStepLogPersistenceAdapter(
    private val jpaStore: QuestionGenerationStepLogJpaStore,
) : QuestionGenerationStepLogStore {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun save(log: QuestionGenerationStepLog) {
        val entity = QuestionGenerationStepLogEntity.Companion.fromDomain(log)
        jpaStore.save(entity)
    }

    @Transactional(readOnly = true)
    override fun loadAllByGenerationId(
        generationId: QuestionGenerationId,
    ): List<QuestionGenerationStepLog> =
        jpaStore
            .findAllByGenerationIdOrderByOccurredAtAsc(generationId.value)
            .map { it.toDomain() }
}
