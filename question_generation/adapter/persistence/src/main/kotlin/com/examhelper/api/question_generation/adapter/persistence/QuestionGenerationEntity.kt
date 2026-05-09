package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory
import com.examhelper.api.question_generation.domain.QuestionGeneration
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStatus
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import com.examhelper.api.question_generation.domain.vo.QuestionGenerationTopic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name    = "question_generations",
    indexes = [
        Index(name = "idx_qg_status",     columnList = "status"),
        Index(name = "idx_qg_created_at", columnList = "created_at"),
    ]
)
class QuestionGenerationEntity(
    @Id
    val id: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val subject: Subject,

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, length = 30)
    val questionType: QuestionType,

    @Enumerated(EnumType.STRING)
    @Column(name = "question_sub_type", length = 30)
    val questionSubType: QuestionSubType?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val difficulty: DifficultyLevel,

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_category", nullable = false, length = 10)
    val topicCategory: TopicCategory,

    @Column(name = "topic_keyword", length = 100)
    val topicKeyword: String?,

    @Column(name = "topic_description", length = 500)
    val topicDescription: String?,

    @Column(nullable = false)
    val quantity: Int,

    @Column(name = "frame_search_top_k", nullable = false)
    val frameSearchTopK: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: QuestionGenerationStatus,

    @Column(name = "failure_reason", length = 500)
    var failureReason: String?,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant
) {
    companion object {
        fun fromDomain(domain: QuestionGeneration): QuestionGenerationEntity {
            return QuestionGenerationEntity(
                id              = domain.id.value,
                subject         = domain.request.subject,
                questionType    = domain.request.questionType,
                questionSubType = domain.request.questionSubType,
                difficulty      = domain.request.difficulty,
                topicCategory   = domain.request.topic.category,
                topicKeyword    = domain.request.topic.keyword,
                topicDescription= domain.request.topic.description,
                quantity        = domain.request.quantity,
                frameSearchTopK = domain.request.frameSearchTopK,
                status          = domain.status,
                failureReason   = domain.failureReason,
                createdAt       = domain.createdAt,
                updatedAt       = domain.updatedAt
            )
        }
    }

    fun toDomain(): QuestionGeneration {
        return QuestionGeneration.of(
            id = QuestionGenerationId(id),
            request = QuestionGenerationRequest(
                subject = subject,
                questionType = questionType,
                questionSubType = questionSubType,
                difficulty = difficulty,
                topic = QuestionGenerationTopic(
                    category = topicCategory,
                    keyword = topicKeyword,
                    description = topicDescription
                ),
                quantity = quantity,
                frameSearchTopK = frameSearchTopK
            ),
            status        = status,
            failureReason = failureReason,
            createdAt     = createdAt,
            updatedAt     = updatedAt
        )
    }
}
