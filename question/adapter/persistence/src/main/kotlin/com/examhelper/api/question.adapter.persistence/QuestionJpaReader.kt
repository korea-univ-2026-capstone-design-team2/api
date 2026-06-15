package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.port.inbound.view.QuestionSummaryView
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface QuestionJpaReader : JpaRepository<QuestionEntity, Long> {
    fun findEntityById(id: Long): QuestionEntity?
    fun findEntityByIdAndMemberId(id: Long, memberId: Long): QuestionEntity?
    fun findAllByIdInAndMemberId(ids: List<Long>, memberId: Long): List<QuestionEntity>
    fun findAllByGenerationIdAndMemberId(generationId: Long, memberId: Long): List<QuestionEntity>

    @Query("""
        SELECT new com.examhelper.api.question.port.inbound.view.QuestionSummaryView(
            q.id,
            q.generationId,
            q.subject,
            q.questionType,
            q.difficulty,
            q.status
        )
        FROM QuestionEntity q
        WHERE q.memberId = :memberId
          AND (:subject IS NULL OR q.subject = :subject)
          AND (:questionType IS NULL OR q.questionType = :questionType)
          AND (:difficulty IS NULL OR q.difficulty = :difficulty)
    """)
    fun findSummaries(
        @Param("memberId") memberId: Long,
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        pageable: Pageable,
    ): List<QuestionSummaryView>

    @Query("""
        SELECT COUNT(q)
        FROM QuestionEntity q
        WHERE q.memberId = :memberId
          AND (:subject IS NULL OR q.subject = :subject)
          AND (:questionType IS NULL OR q.questionType = :questionType)
          AND (:difficulty IS NULL OR q.difficulty = :difficulty)
    """)
    fun countByFilter(
        @Param("memberId") memberId: Long,
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
    ): Long
}
