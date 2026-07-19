package com.examhelper.api.exam.adapter.persistence

import com.examhelper.api.exam.domain.Exam
import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.domain.type.ExamTopic
import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.domain.vo.ExamMetadata
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.Instant

@Entity
@Table(
    name = "exams",
    indexes = [
        Index(name = "idx_exams_status", columnList = "status"),
        Index(name = "idx_exams_created_at", columnList = "created_at"),
    ],
    uniqueConstraints = [
        UniqueConstraint(name = "uk_exams_generation_id", columnNames = ["generation_id"])
    ]
)
class ExamEntity(
    @Id
    val id: Long,

    @Column(nullable = false)
    val memberId: Long,

    @Column(nullable = false, length = 200)
    val title: String,

    // ── ExamMetadata ────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val subject: Subject,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val questionType: QuestionType,

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    val questionSubType: QuestionSubType?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    val difficulty: DifficultyLevel,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    val topicCategory: TopicCategory,

    @Column(length = 100)
    val topicKeyword: String?,

    @Column(length = 500)
    val topicDescription: String?,

    @Column(nullable = false)
    val targetQuestionCount: Int,

    // ── ExamStatus ──────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ExamStatus,

    // ── ExamGenerationResult (nullable) ────────────
    @Column
    var generationId: Long?,

    @Column
    var generationSuccessCount: Int?,

    @Column
    var generationFailCount: Int?,

    // ── Timestamps ──────────────────────────────────
    @Column(nullable = false, updatable = false)
    val createdAt: Instant,

    @Column(nullable = false)
    var updatedAt: Instant,

    // ── Children ────────────────────────────────────
    @OneToMany(
        mappedBy = "exam",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY,
    )
    @OrderBy("ordering ASC")
    val items: MutableList<ExamItemEntity> = mutableListOf(),
) {
    companion object {
        fun fromDomain(domain: Exam): ExamEntity {
            val entity = ExamEntity(
                id = domain.id.value,
                memberId = domain.memberId.value,
                title = domain.title,
                subject = domain.metadata.subject,
                questionType = domain.metadata.questionType,
                questionSubType = domain.metadata.questionSubType,
                difficulty = domain.metadata.difficulty,
                topicCategory = domain.metadata.topic.category,
                topicKeyword = domain.metadata.topic.keyword,
                topicDescription = domain.metadata.topic.description,
                targetQuestionCount = domain.metadata.targetQuestionCount,
                status = domain.status,
                generationId = domain.generationResult?.generationId?.value,
                generationSuccessCount = domain.generationResult?.successCount,
                generationFailCount = domain.generationResult?.failCount,
                createdAt = domain.createdAt,
                updatedAt = domain.updatedAt,
            )
            domain.items
                .map { ExamItemEntity.fromDomain(it, entity) }
                .forEach { entity.items.add(it) }
            return entity
        }
    }

    fun toDomain(): Exam = Exam.of(
        id = ExamId(id),
        memberId = MemberId(memberId),
        title = title,
        metadata = ExamMetadata(
            subject = subject,
            questionType = questionType,
            questionSubType = questionSubType,
            difficulty = difficulty,
            topic = ExamTopic(
                category = topicCategory,
                keyword = topicKeyword,
                description = topicDescription,
            ),
            targetQuestionCount = targetQuestionCount,
        ),
        status = status,
        items = items.map { it.toDomain() },
        generationResult = toGenerationResult(),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    fun update(domain: Exam) {
        status = domain.status
        updatedAt = domain.updatedAt
        generationId = domain.generationResult?.generationId?.value
        generationSuccessCount = domain.generationResult?.successCount
        generationFailCount = domain.generationResult?.failCount

        // items 동기화: orphanRemoval = true이므로 clear 후 re-add
        val existingIds = items.map { it.id }.toSet()
        val domainIds = domain.items.map { it.id.value }.toSet()

        // 삭제된 것 제거
        items.removeIf { it.id !in domainIds }

        // 새로 추가된 것만 추가
        domain.items
            .filter { it.id.value !in existingIds }
            .map { ExamItemEntity.fromDomain(it, this) }
            .forEach { items.add(it) }
    }

    private fun toGenerationResult(): ExamGenerationResult? {
        val generationId = generationId ?: return null

        val success = generationSuccessCount
        val fail = generationFailCount

        check((success == null) == (fail == null)) {
            "generationSuccessCount와 generationFailCount 상태가 일치하지 않습니다. " +
                    "generationId=$generationId, successCount=$success, failCount=$fail"
        }

        return ExamGenerationResult(
            generationId = QuestionGenerationId(generationId),
            successCount = success,
            failCount = fail,
        )
    }
}
