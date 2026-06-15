package com.examhelper.api.exam.adapter.persistence

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.port.inbound.view.ExamSummaryView
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExamJpaReader : JpaRepository<ExamEntity, Long> {
    @EntityGraph(attributePaths = ["items"])
    fun findEntityById(id: Long): ExamEntity?

    @EntityGraph(attributePaths = ["items"])
    fun findEntityByIdAndMemberId(id: Long, memberId: Long): ExamEntity?

    fun existsByIdAndMemberId(id: Long, memberId: Long): Boolean

    @Query("""
        SELECT new com.examhelper.api.exam.port.inbound.view.ExamSummaryView(
            e.id,
            e.title,
            e.subject,
            e.questionType,
            e.difficulty,
            e.targetQuestionCount,
            SIZE(e.items),
            e.status,
            e.createdAt
        )
        FROM ExamEntity e
        WHERE e.memberId = :memberId
          AND (:subject      IS NULL OR e.subject      = :subject)
          AND (:questionType IS NULL OR e.questionType = :questionType)
          AND (:difficulty   IS NULL OR e.difficulty   = :difficulty)
          AND (:status       IS NULL OR e.status       = :status)
        ORDER BY e.createdAt DESC
    """)
    fun findSummaries(
        @Param("memberId") memberId: Long,
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        @Param("status") status: ExamStatus?,
        pageable: Pageable,
    ): List<ExamSummaryView>

    @Query("""
        SELECT COUNT(e)
        FROM ExamEntity e
        WHERE e.memberId = :memberId
          AND (:subject      IS NULL OR e.subject      = :subject)
          AND (:questionType IS NULL OR e.questionType = :questionType)
          AND (:difficulty   IS NULL OR e.difficulty   = :difficulty)
          AND (:status       IS NULL OR e.status       = :status)
    """)
    fun countByFilter(
        @Param("memberId") memberId: Long,
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        @Param("status") status: ExamStatus?,
    ): Long
}
