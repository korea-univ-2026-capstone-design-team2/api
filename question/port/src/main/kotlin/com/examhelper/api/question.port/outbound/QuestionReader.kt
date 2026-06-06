package com.examhelper.api.question.port.outbound

import com.examhelper.api.question.port.inbound.view.CorrectAnswerView
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemPaperView
import com.examhelper.api.question.port.inbound.view.QuestionItemReviewView
import com.examhelper.api.question.port.inbound.view.QuestionItemSummaryView
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import com.examhelper.api.question.port.inbound.view.QuestionSummaryView

interface QuestionReader {
    // ── Group 단위 ─────────────────────────────────────────
    fun findPaperById(id: Long): QuestionPaperView?
    fun findPapersByIds(ids: List<Long>): List<QuestionPaperView>
    fun findReviewById(id: Long): QuestionReviewView?
    fun findReviewsByIds(ids: List<Long>): List<QuestionReviewView>
    fun findDetailById(id: Long): QuestionDetailView?
    fun findAll(filter: QuestionFilter): List<QuestionSummaryView>
    fun count(filter: QuestionFilter): Long

    // ── Item 단위 ──────────────────────────────────────────
    fun findItemPaperById(id: Long): QuestionItemPaperView?
    fun findItemReviewById(id: Long): QuestionItemReviewView?
    fun findItemDetailById(id: Long): QuestionItemDetailView?
    fun findAllItems(filter: QuestionItemFilter): List<QuestionItemSummaryView>
    fun countItems(filter: QuestionItemFilter): Long
    fun findCorrectAnswersByQuestionItemIds(questionItemIds: List<Long>): List<CorrectAnswerView>
}
