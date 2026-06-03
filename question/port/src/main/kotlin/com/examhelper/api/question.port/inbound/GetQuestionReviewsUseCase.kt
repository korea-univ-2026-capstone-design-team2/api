package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.query.GetQuestionReviewsQuery
import com.examhelper.api.question.port.inbound.view.QuestionReviewView

interface GetQuestionReviewsUseCase {
    fun execute(query: GetQuestionReviewsQuery): List<QuestionReviewView>
}
