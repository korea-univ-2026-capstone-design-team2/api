package com.examhelper.api.learning_statistics.adapter.persistence.projection

import com.examhelper.api.learning_statistics.adapter.persistence.projection.projection.DailyLearningRecordProjection
import com.examhelper.api.learning_statistics.adapter.persistence.projection.projection.DailyLearningSummaryProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface DailyLearningStatJpaReader : JpaRepository<DailyLearningStatEntity, Long> {
    @Query("""
        SELECT
            COALESCE(SUM(s.questionCount), 0) AS totalQuestions,
            COALESCE(SUM(s.correctCount), 0) AS totalCorrect,
            COALESCE(SUM(s.studySeconds), 0) AS totalStudySeconds
        FROM DailyLearningStatEntity s
        WHERE s.memberId = :memberId
          AND s.studyDate BETWEEN :from AND :to
    """)
    fun findSummary(
        @Param("memberId") memberId: Long,
        @Param("from") from: LocalDate,
        @Param("to") to: LocalDate,
    ): DailyLearningSummaryProjection?

    @Query("""
        SELECT
            s.studyDate AS date,
            SUM(s.questionCount) AS questionCount,
            SUM(s.correctCount) AS correctCount,
            SUM(s.studySeconds) AS studySeconds
        FROM DailyLearningStatEntity s
        WHERE s.memberId = :memberId
          AND s.studyDate BETWEEN :from AND :to
        GROUP BY s.studyDate
        ORDER BY s.studyDate
    """
    )
    fun findDailyRecords(
        memberId: Long,
        from: LocalDate,
        to: LocalDate,
    ): List<DailyLearningRecordProjection>
}
