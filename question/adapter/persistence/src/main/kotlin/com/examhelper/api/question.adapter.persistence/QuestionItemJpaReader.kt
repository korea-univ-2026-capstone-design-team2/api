package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.port.inbound.view.QuestionItemSummaryView
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface QuestionItemJpaReader : JpaRepository<QuestionItemEntity, Long> {

    fun findEntityById(id: Long): QuestionItemEntity?

    fun findAllByQuestionId(questionId: Long): List<QuestionItemEntity>

    @Query("""
        SELECT new com.examhelper.api.question.port.inbound.view.QuestionItemSummaryView(
            qi.id,
            qi.question.id,
            qi.subject,
            qi.questionType,
            qi.questionSubType,
            qi.difficulty,
            qi.status,
            qi.qualityScore
        )
        FROM QuestionItemEntity qi
        WHERE (:subject IS NULL OR qi.subject = :subject)
          AND (:questionType IS NULL OR qi.questionType = :questionType)
          AND (:questionSubType IS NULL OR qi.questionSubType = :questionSubType)
          AND (:difficulty IS NULL OR qi.difficulty = :difficulty)
          AND (:questionId IS NULL OR qi.question.id = :questionId)
    """)
    fun findSummaries(
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("questionSubType") questionSubType: QuestionSubType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        @Param("questionId") questionId: Long?,
        pageable: Pageable,
    ): List<QuestionItemSummaryView>

    @Query("""
        SELECT COUNT(qi)
        FROM QuestionItemEntity qi
        WHERE (:subject IS NULL OR qi.subject = :subject)
          AND (:questionType IS NULL OR qi.questionType = :questionType)
          AND (:questionSubType IS NULL OR qi.questionSubType = :questionSubType)
          AND (:difficulty IS NULL OR qi.difficulty = :difficulty)
          AND (:questionId IS NULL OR qi.question.id = :questionId)
    """)
    fun countByFilter(
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("questionSubType") questionSubType: QuestionSubType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        @Param("questionId") questionId: Long?
    ): Long
}
