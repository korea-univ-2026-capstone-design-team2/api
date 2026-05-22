package com.examhelper.api.question.application

import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.GetQuestionDetailUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionDetailQuery
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service

@Service
class GetQuestionDetailService(
    private val questionReader: QuestionReader,
) : GetQuestionDetailUseCase {
    override fun execute(query: GetQuestionDetailQuery): QuestionDetailView =
        questionReader.findDetailById(query.questionId)
            ?: throw QuestionException.NotFound(query.questionId)
}
