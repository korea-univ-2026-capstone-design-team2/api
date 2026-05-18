package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.adapter.persistence.converter.PassageTopicConverter
import com.examhelper.api.question.adapter.persistence.converter.QuestionIdsConverter
import com.examhelper.api.question.adapter.persistence.converter.SharedQuestionContextConverter
import com.examhelper.api.question.adapter.persistence.record.PassageTopicRecord
import com.examhelper.api.question.adapter.persistence.record.SharedQuestionContextRecord
import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.vo.QuestionMetadata
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "questions")
class QuestionEntity(
    @Id
    val id: Long,

    // ── 역추적 ─────────────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val generationId: Long,

    // ── 검색/필터 컬럼 ─────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val subject: Subject,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val questionType: QuestionType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val difficulty: DifficultyLevel,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: QuestionStatus,

    // ── JSON 컬럼 ──────────────────────────────────────────
    @Convert(converter = SharedQuestionContextConverter::class)
    @Column(columnDefinition = "JSON")
    val sharedContext: SharedQuestionContextRecord?,

    @Convert(converter = PassageTopicConverter::class)
    @Column(columnDefinition = "JSON")
    val passageTopic: PassageTopicRecord?,

    @Convert(converter = QuestionIdsConverter::class)
    @Column(nullable = false, columnDefinition = "JSON")
    val questionItemIds: List<Long>,

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
            subject = domain.metadata.subject,
            questionType = domain.metadata.questionType,
            difficulty = domain.metadata.difficulty,
            status = domain.status,
            sharedContext = SharedQuestionContextRecord.fromDomain(domain.sharedContext),
            passageTopic = domain.metadata.passageTopic?.let { PassageTopicRecord.fromDomain(it) },
            questionItemIds = domain.questionItemIds.map { it.value },
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    fun toDomain(): Question = Question.of(
        id = QuestionId(id),
        generationId = QuestionGenerationId(generationId),
        sharedContext = sharedContext?.toDomain(),
        questionItemIds = questionItemIds.map { QuestionItemId(it) },
        metadata = QuestionMetadata(
            subject = subject,
            questionType = questionType,
            difficulty = difficulty,
            passageTopic = passageTopic?.toDomain(),
        ),
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
