package com.examhelper.api.question.port.outbound

import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import com.examhelper.api.question.port.inbound.view.QuestionSummaryView

interface QuestionReader {
    fun findPaperById(id: Long): QuestionPaperView?       // 문제풀이용
    fun findReviewById(id: Long): QuestionReviewView?         // 풀이 확인용
    fun findDetailById(id: Long): QuestionDetailView?         // 내부 관리용
    fun findAll(filter: QuestionFilter): List<QuestionSummaryView>
    fun count(filter: QuestionFilter): Long
}
