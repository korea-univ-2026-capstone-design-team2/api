package com.examhelper.api.question_generation.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface QuestionGenerationJpaStore: JpaRepository<QuestionGenerationEntity, Long>
