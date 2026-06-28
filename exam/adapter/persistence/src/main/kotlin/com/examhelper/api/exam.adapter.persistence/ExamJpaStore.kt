package com.examhelper.api.exam.adapter.persistence

import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints

interface ExamJpaStore: JpaRepository<ExamEntity, Long> {
    fun findByGenerationId(generationId: Long): ExamEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    @Query("SELECT e FROM ExamEntity e WHERE e.generationId = :generationId")
    fun findByGenerationIdForUpdate(generationId: Long): ExamEntity?
}
