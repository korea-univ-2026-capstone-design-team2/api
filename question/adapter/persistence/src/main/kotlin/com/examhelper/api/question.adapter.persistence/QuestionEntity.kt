package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question.adapter.persistence.converter.PassageTopicConverter
import com.examhelper.api.question.adapter.persistence.converter.SharedQuestionContextConverter
import com.examhelper.api.question.adapter.persistence.record.PassageTopicRecord
import com.examhelper.api.question.adapter.persistence.record.SharedQuestionContextRecord
import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.vo.QuestionMetadata
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "questions")
class QuestionEntity(
    @Id
    val id: Long,

    @Column
    val memberId: Long,

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
    @Column
    val questionSubType: QuestionSubType?,

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

    @OneToMany(
        mappedBy = "question",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    private val items: MutableList<QuestionItemEntity>,

    // ── 타임스탬프 ─────────────────────────────────────────
    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    val updatedAt: Instant,
) {
    companion object {
        fun fromDomain(domain: Question): QuestionEntity {
            val entity = QuestionEntity(
                id = domain.id.value,
                memberId = domain.memberId.value,
                generationId = domain.generationId.value,
                subject = domain.metadata.subject,
                questionType = domain.metadata.questionType,
                questionSubType = domain.metadata.questionSubType,
                difficulty = domain.metadata.difficulty,
                status = domain.status,
                sharedContext = SharedQuestionContextRecord.fromDomain(domain.sharedContext),
                passageTopic = domain.metadata.passageTopic?.let { PassageTopicRecord.fromDomain(it) },
                items = mutableListOf(),
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
            )

            val itemEntities = domain.items.map {
                QuestionItemEntity.fromDomain(
                    domain = it,
                    question = entity,
                )
            }

            entity.items.addAll(itemEntities)

            return entity
        }
    }

    fun toDomain(): Question = Question.of(
        id = QuestionId(id),
        memberId = MemberId(memberId),
        generationId = QuestionGenerationId(generationId),
        sharedContext = sharedContext?.toDomain(),
        items = items.map { it.toDomain() },
        metadata = QuestionMetadata(
            subject = subject,
            questionType = questionType,
            questionSubType = questionSubType,
            difficulty = difficulty,
            passageTopic = passageTopic?.toDomain(),
        ),
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
