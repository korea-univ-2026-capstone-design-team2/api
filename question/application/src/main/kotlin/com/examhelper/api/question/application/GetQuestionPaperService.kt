package com.examhelper.api.question.application

import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.GetQuestionPaperUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionPaperQuery
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetQuestionPaperService(
    private val questionReader: QuestionReader,
) : GetQuestionPaperUseCase {
    override fun execute(query: GetQuestionPaperQuery): QuestionPaperView =
        questionReader.findPaperById(query.questionId)
            ?: throw QuestionException.NotFound(query.questionId.toString())
}
