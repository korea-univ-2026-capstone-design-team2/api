package com.examhelper.api.exam.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ExamJpaStore: JpaRepository<ExamEntity, Long>
