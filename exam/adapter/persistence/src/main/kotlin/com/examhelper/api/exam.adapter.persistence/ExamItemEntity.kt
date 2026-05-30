package com.examhelper.api.exam.adapter.persistence

import com.examhelper.api.exam.domain.entity.ExamItem
import com.examhelper.api.kernel.identifier.ExamItemId
import com.examhelper.api.kernel.identifier.QuestionId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name = "exam_items",
    indexes = [
        Index(name = "idx_exam_items_exam_id", columnList = "exam_id"),
        Index(name = "idx_exam_items_question_id", columnList = "question_id"),
    ]
)
class ExamItemEntity(
    @Id
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    val exam: ExamEntity,

    @Column(nullable = false)
    val questionId: Long,

    @Column(nullable = false)
    val ordering: Int,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant
) {
    companion object {
        fun fromDomain(domain: ExamItem, examEntity: ExamEntity): ExamItemEntity =
            ExamItemEntity(
                id = domain.id.value,
                exam = examEntity,
                questionId = domain.questionId.value,
                ordering = domain.ordering,
                createdAt = domain.createdAt,
            )
    }

    fun toDomain(): ExamItem = ExamItem(
        id = _root_ide_package_.com.examhelper.api.kernel.identifier.ExamItemId(id),
        questionId = _root_ide_package_.com.examhelper.api.kernel.identifier.QuestionId(questionId),
        ordering = ordering,
        createdAt = createdAt,
    )
}
