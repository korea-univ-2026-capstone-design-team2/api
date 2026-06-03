package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.query.GetQuestionDetailQuery
import com.examhelper.api.question.port.inbound.view.QuestionDetailView

interface GetQuestionDetailUseCase {
    fun execute(query: GetQuestionDetailQuery): QuestionDetailView
}
