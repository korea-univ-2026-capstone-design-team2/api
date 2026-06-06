package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.adapter.persistence.projection.ExamAttemptAnswerResultProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExamAttemptAnswerJpaReader : JpaRepository<ExamAttemptAnswerEntity, ExamAttemptAnswerPk> {
    @Query("""
        SELECT new com.examhelper.api.exam_attempt.adapter.persistence.projection.ExamAttemptAnswerResultProjection(
            a.id.questionItemId,
            a.selectedNumber,
            a.timeSpentSeconds,
            a.markedUnknown,
            a.bookmarked
        )
        FROM ExamAttemptAnswerEntity a
        WHERE a.attempt.id = :attemptId
    """)
    fun findResultAnswers(
        @Param("attemptId") attemptId: Long
    ): List<ExamAttemptAnswerResultProjection>
}
