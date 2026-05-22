package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.adapter.persistence.converter.AnswerSheetConverter
import com.examhelper.api.question.adapter.persistence.converter.ExplanationConverter
import com.examhelper.api.question.adapter.persistence.converter.QuestionItemContentConverter
import com.examhelper.api.question.adapter.persistence.record.AnswerSheetRecord
import com.examhelper.api.question.adapter.persistence.record.ExplanationRecord
import com.examhelper.api.question.adapter.persistence.record.QuestionItemContentRecord
import com.examhelper.api.question.domain.QuestionItem
import com.examhelper.api.question.domain.type.QuestionItemStatus
import com.examhelper.api.question.domain.vo.QualityScore
import com.examhelper.api.question.domain.vo.QuestionItemMetadata
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant


/**
MySql DB 스키마를 JPA Entity를 통해 정의합니다.
fromDomain(): Question 도메인 객체를 QuestionEntity로 변환하는 팩토리 메서드입니다.
toDomain(): QuestionEntity 객체를 Question 도메인 객체로 변환하는 메서드입니다.
*/
@Entity
@Table(name = "question_items")
class QuestionItemEntity(
    @Id
    val id: Long,

    // ── 역추적 / 관계 ──────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val generationId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    val question: QuestionEntity,

    // ── 검색/필터 컬럼 ─────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val subject: Subject,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val questionType: QuestionType,

    @Enumerated(EnumType.STRING)
    @Column
    val questionSubType: QuestionSubType?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val difficulty: DifficultyLevel,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: QuestionItemStatus,

    @Column
    val qualityScore: Double?,

    // ── JSON 컬럼 ──────────────────────────────────────────
    @Convert(converter = QuestionItemContentConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val content: QuestionItemContentRecord,

    @Convert(converter = AnswerSheetConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val answerSheet: AnswerSheetRecord,

    @Convert(converter = ExplanationConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val explanation: ExplanationRecord,

    // ── 타임스탬프 ─────────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant,
) {
    companion object {
        fun fromDomain(domain: QuestionItem, question: QuestionEntity): QuestionItemEntity = QuestionItemEntity(
            id = domain.id.value,
            generationId = domain.generationId.value,
            question = question,
            subject = domain.metadata.subject,
            questionType = domain.metadata.questionType,
            questionSubType = domain.metadata.questionSubType,
            difficulty = domain.metadata.difficulty,
            status = domain.status,
            qualityScore = domain.qualityScore?.value,
            content = QuestionItemContentRecord.fromDomain(domain.content),
            answerSheet = AnswerSheetRecord.fromDomain(domain.answerSheet),
            explanation = ExplanationRecord.fromDomain(domain.explanation),
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    fun toDomain(): QuestionItem = QuestionItem.of(
        id = QuestionItemId(id),
        generationId = QuestionGenerationId(generationId),
        content = content.toDomain(),
        answerSheet = answerSheet.toDomain(),
        metadata = QuestionItemMetadata(
            subject = subject,
            questionType = questionType,
            questionSubType = questionSubType,
            difficulty = difficulty
        ),
        explanation = explanation.toDomain(),
        qualityScore = qualityScore?.let { QualityScore(it) },
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
