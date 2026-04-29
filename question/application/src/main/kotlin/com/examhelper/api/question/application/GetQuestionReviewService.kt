package com.examhelper.api.question.application

import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.GetQuestionReviewUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionReviewQuery
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetQuestionReviewService(
    private val questionReader: QuestionReader,
) : GetQuestionReviewUseCase {
    override fun execute(query: GetQuestionReviewQuery): QuestionReviewView =
        questionReader.findReviewById(query.questionId)
            ?: throw QuestionException.NotFound(query.questionId.toString())
}
