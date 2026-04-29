package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.question.domain.type.DifficultyLevel
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.type.QuestionType
import com.examhelper.api.question.domain.type.Subject
import com.examhelper.api.question.port.inbound.view.QuestionSummaryView
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface QuestionJpaReader : JpaRepository<QuestionEntity, Long> {

    // ── 단건 상세 조회 ──────────────────────────────────────
    // JSON 컬럼 Converter가 동작해야 하므로 Entity 전체 로드
    // toDetailView() 변환은 QuestionQueryAdapter에서 담당
    fun findEntityById(id: Long): QuestionEntity?

    // ── 목록 조회 — 메타데이터 컬럼만 Projection ──────────
    // JSON 컬럼 제외 → 불필요한 역직렬화 없음
    @Query("""
        SELECT new com.examhelper.api.question.port.inbound.view.QuestionSummaryView(
            q.id,
            q.questionSetId,
            q.subject,
            q.questionType,
            q.questionSubType,
            q.difficulty,
            q.status,
            q.qualityScore
        )
        FROM QuestionEntity q
        WHERE (:subject IS NULL OR q.subject = :subject)
          AND (:questionType IS NULL OR q.questionType = :questionType)
          AND (:difficulty IS NULL OR q.difficulty = :difficulty)
          AND (:status IS NULL OR q.status = :status)
    """)
    fun findSummaries(
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        @Param("status") status: QuestionStatus?,
        pageable: Pageable,
    ): List<QuestionSummaryView>

    // ── count 쿼리 ─────────────────────────────────────────
    @Query("""
        SELECT COUNT(q)
        FROM QuestionEntity q
        WHERE (:subject IS NULL OR q.subject = :subject)
          AND (:questionType IS NULL OR q.questionType = :questionType)
          AND (:difficulty IS NULL OR q.difficulty = :difficulty)
          AND (:status IS NULL OR q.status = :status)
    """)
    fun countByFilter(
        @Param("subject") subject: Subject?,
        @Param("questionType") questionType: QuestionType?,
        @Param("difficulty") difficulty: DifficultyLevel?,
        @Param("status") status: QuestionStatus?,
    ): Long
}
