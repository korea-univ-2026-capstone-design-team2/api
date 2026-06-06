package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.adapter.persistence.projection.ExamAttemptResultProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExamAttemptJpaReader : JpaRepository<ExamAttemptEntity, Long> {
    @Query("""
        SELECT new com.examhelper.api.exam_attempt.adapter.persistence.projection.ExamAttemptResultProjection(
            a.id,
            a.examId,
            a.memberId,
            a.status,
            a.startedAt,
            a.submittedAt
        )
        FROM ExamAttemptEntity a
        WHERE a.id = :attemptId
    """)
    fun findResultAttempt(
        @Param("attemptId") attemptId: Long
    ): ExamAttemptResultProjection?
}
