package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionSetId
import com.examhelper.api.question.adapter.persistence.converter.AnswerSheetConverter
import com.examhelper.api.question.adapter.persistence.converter.ExplanationConverter
import com.examhelper.api.question.adapter.persistence.converter.FrameReferenceConverter
import com.examhelper.api.question.adapter.persistence.converter.PassageTopicConverter
import com.examhelper.api.question.adapter.persistence.converter.QuestionContentConverter
import com.examhelper.api.question.adapter.persistence.record.AnswerSheetRecord
import com.examhelper.api.question.adapter.persistence.record.ExplanationRecord
import com.examhelper.api.question.adapter.persistence.record.FrameReferenceRecord
import com.examhelper.api.question.adapter.persistence.record.PassageTopicRecord
import com.examhelper.api.question.adapter.persistence.record.QuestionContentRecord
import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.domain.type.DifficultyLevel
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.type.QuestionSubType
import com.examhelper.api.question.domain.type.QuestionType
import com.examhelper.api.question.domain.type.Subject
import com.examhelper.api.question.domain.vo.QualityScore
import com.examhelper.api.question.domain.vo.QuestionMetadata
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant


/**
MySql DB 스키마를 JPA Entity를 통해 정의합니다.
fromDomain(): Question 도메인 객체를 QuestionEntity로 변환하는 팩토리 메서드입니다.
toDomain(): QuestionEntity 객체를 Question 도메인 객체로 변환하는 메서드입니다.
*/
@Entity
@Table(name = "questions")
class QuestionEntity(
    @Id
    val id: Long,

    // ── 역추적 / 관계 ──────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val generationId: Long,

    @Column
    val questionSetId: Long?,

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
    val status: QuestionStatus,

    @Column
    val qualityScore: Double?,

    // ── JSON 컬럼 ──────────────────────────────────────────
    @Convert(converter = QuestionContentConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val content: QuestionContentRecord,

    @Convert(converter = AnswerSheetConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val answerSheet: AnswerSheetRecord,

    @Convert(converter = ExplanationConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val explanation: ExplanationRecord,

    @Convert(converter = FrameReferenceConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val sourceFrame: FrameReferenceRecord,

    @Convert(converter = PassageTopicConverter::class)
    @Column(columnDefinition = "JSON")
    val passageTopic: PassageTopicRecord?,

    // ── 타임스탬프 ─────────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant,
) {
    companion object {
        fun fromDomain(domain: Question): QuestionEntity = QuestionEntity(
            id = domain.id.value,
            generationId = domain.generationId.value,
            questionSetId = domain.questionSetId?.value,
            subject = domain.metadata.subject,
            questionType = domain.metadata.questionType,
            questionSubType = domain.metadata.questionSubType,
            difficulty = domain.metadata.difficulty,
            status = domain.status,
            qualityScore = domain.qualityScore?.value,
            content = QuestionContentRecord.fromDomain(domain.content),
            answerSheet = AnswerSheetRecord.fromDomain(domain.answerSheet),
            explanation = ExplanationRecord.fromDomain(domain.explanation),
            sourceFrame = FrameReferenceRecord.fromDomain(domain.sourceFrame),
            passageTopic = domain.metadata.passageTopic?.let { PassageTopicRecord.fromDomain(it) },
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    fun toDomain(): Question = Question.of(
        id = QuestionId(id),
        generationId = QuestionGenerationId(generationId),
        content = content.toDomain(),
        answerSheet = answerSheet.toDomain(),
        explanation = explanation.toDomain(),
        sourceFrame = sourceFrame.toDomain(),
        metadata = QuestionMetadata(
            subject = subject,
            questionType = questionType,
            questionSubType = questionSubType,
            difficulty = difficulty,
            passageTopic = passageTopic?.toDomain(),
        ),
        qualityScore = qualityScore?.let { QualityScore(it) },
        questionSetId = questionSetId?.let { QuestionSetId(it) },
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
