package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.query.GetQuestionPaperQuery
import com.examhelper.api.question.port.inbound.view.QuestionPaperView

interface GetQuestionPaperUseCase {
    fun execute(query: GetQuestionPaperQuery): QuestionPaperView
}
