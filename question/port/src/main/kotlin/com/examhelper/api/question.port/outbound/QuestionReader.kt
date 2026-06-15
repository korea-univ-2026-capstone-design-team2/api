package com.examhelper.api.question.port.outbound

import com.examhelper.api.question.port.inbound.view.CorrectAnswerView
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemMetadataView
import com.examhelper.api.question.port.inbound.view.QuestionItemPaperView
import com.examhelper.api.question.port.inbound.view.QuestionItemReviewView
import com.examhelper.api.question.port.inbound.view.QuestionItemSummaryView
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import com.examhelper.api.question.port.inbound.view.QuestionSummaryView

interface QuestionReader {
    // ── Group 단위 ─────────────────────────────────────────
    fun findPaperById(id: Long, memberId: Long): QuestionPaperView?
    fun findPapersByIds(ids: List<Long>, memberId: Long): List<QuestionPaperView>
    fun findPapersByGenerationId(generationId: Long, memberId: Long): List<QuestionPaperView>
    fun findReviewById(id: Long, memberId: Long): QuestionReviewView?
    fun findReviewsByIds(ids: List<Long>, memberId: Long): List<QuestionReviewView>
    fun findDetailById(id: Long): QuestionDetailView?
    fun findAll(filter: QuestionFilter): List<QuestionSummaryView>
    fun count(filter: QuestionFilter): Long

    // ── Item 단위 ──────────────────────────────────────────
    fun findItemSummaries(questionItemIds: List<Long>): List<QuestionItemMetadataView>
    fun findCorrectAnswersByQuestionItemIds(questionItemIds: List<Long>): List<CorrectAnswerView>
}
