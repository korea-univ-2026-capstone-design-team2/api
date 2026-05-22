package com.examhelper.api.question.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface QuestionJpaStore: JpaRepository<QuestionEntity, Long>
