package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.query.GetQuestionReviewQuery
import com.examhelper.api.question.port.inbound.view.QuestionReviewView

interface GetQuestionReviewUseCase {
    fun execute(query: GetQuestionReviewQuery): QuestionReviewView
}
