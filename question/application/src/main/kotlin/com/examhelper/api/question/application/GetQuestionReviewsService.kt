package com.examhelper.api.question.application

import com.examhelper.api.question.port.inbound.GetQuestionReviewsUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionReviewsQuery
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service

@Service
class GetQuestionReviewsService(
    private val questionReader: QuestionReader,
) : GetQuestionReviewsUseCase {
    override fun execute(query: GetQuestionReviewsQuery): List<QuestionReviewView> =
        questionReader.findReviewsByIds(query.questionIds, query.memberId)
}
