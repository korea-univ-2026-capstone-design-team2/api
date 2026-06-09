package com.examhelper.api.question_generation.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface QuestionGenerationStepLogJpaStore : JpaRepository<QuestionGenerationStepLogEntity, Long> {
    fun findAllByGenerationIdOrderByOccurredAtAsc(
        generationId: Long,
    ): List<QuestionGenerationStepLogEntity>
}
