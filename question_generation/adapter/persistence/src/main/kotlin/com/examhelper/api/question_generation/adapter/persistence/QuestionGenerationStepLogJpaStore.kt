package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.question_generation.domain.type.QuestionGenerationStep
import org.springframework.data.jpa.repository.JpaRepository

interface QuestionGenerationStepLogJpaStore
    : JpaRepository<QuestionGenerationStepLogEntity, Long> {

    fun findAllByGenerationIdOrderByOccurredAtAsc(
        generationId: Long,
    ): List<QuestionGenerationStepLogEntity>

    fun findAllByGenerationIdAndStep(
        generationId: Long,
        step        : QuestionGenerationStep,
    ): List<QuestionGenerationStepLogEntity>
}
