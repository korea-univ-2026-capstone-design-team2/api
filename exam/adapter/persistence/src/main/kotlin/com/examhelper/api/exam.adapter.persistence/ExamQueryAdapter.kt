package com.examhelper.api.exam.adapter.persistence

import com.examhelper.api.exam.port.inbound.query.ExamFilter
import com.examhelper.api.exam.port.inbound.view.ExamBaseView
import com.examhelper.api.exam.port.inbound.view.ExamDetailView
import com.examhelper.api.exam.port.inbound.view.ExamItemView
import com.examhelper.api.exam.port.inbound.view.ExamSummaryView
import com.examhelper.api.exam.port.outbound.ExamReader
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class ExamQueryAdapter(
    private val examJpaReader: ExamJpaReader,
) : ExamReader {
    override fun findDetailById(examId: Long): ExamDetailView? =
        examJpaReader.findEntityById(examId)?.toDetailView()

    override fun findBaseById(examId: Long): ExamBaseView? =
        examJpaReader.findEntityById(examId)?.toBaseView()

    // memberId 귀속 검증
    override fun findDetailById(examId: Long, memberId: Long): ExamDetailView? =
        examJpaReader.findEntityByIdAndMemberId(examId, memberId)?.toDetailView()

    override fun findBaseById(examId: Long, memberId: Long): ExamBaseView? =
        examJpaReader.findEntityByIdAndMemberId(examId, memberId)?.toBaseView()

    override fun findSummaries(filter: ExamFilter): List<ExamSummaryView> =
        examJpaReader.findSummaries(
            memberId = filter.memberId,
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty,
            status = filter.status,
            pageable = PageRequest.of(filter.page, filter.size),
        )

    override fun count(filter: ExamFilter): Long =
        examJpaReader.countByFilter(
            memberId = filter.memberId,
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty,
            status = filter.status,
        )

    override fun existsById(examId: Long, memberId: Long): Boolean =
        examJpaReader.existsByIdAndMemberId(examId, memberId)
}

// ── ExamEntity → DetailView ───────────────────────────────────
private fun ExamEntity.toDetailView(): ExamDetailView =
    ExamDetailView(
        examId = id,
        title = title,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        topicCategory = topicCategory,
        topicKeyword = topicKeyword,
        topicDescription = topicDescription,
        targetQuestionCount = targetQuestionCount,
        status = status,
        generationId = generationId,
        generationSuccessCount = generationSuccessCount,
        generationFailCount = generationFailCount,
        items = items.map { it.toItemView() },
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

// ── ExamItemEntity → ItemView ────────────────────────────────
private fun ExamItemEntity.toItemView(): ExamItemView =
    ExamItemView(
        examItemId = id,
        questionId = questionId,
        ordering = ordering,
    )

private fun ExamEntity.toBaseView(): ExamBaseView =
    ExamBaseView(
        examId = id,
        title = title,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        topicCategory = topicCategory,
        topicKeyword = topicKeyword,
        topicDescription = topicDescription,
        targetQuestionCount = targetQuestionCount,
        status = status,
        generationId = generationId,
        generationSuccessCount = generationSuccessCount,
        generationFailCount = generationFailCount,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
