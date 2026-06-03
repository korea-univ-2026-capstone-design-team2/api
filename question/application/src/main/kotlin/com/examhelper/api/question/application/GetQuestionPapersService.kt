package com.examhelper.api.question.application

import com.examhelper.api.question.port.inbound.GetQuestionPapersUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionPapersQuery
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service

@Service
class GetQuestionPapersService(
    private val questionReader: QuestionReader,
) : GetQuestionPapersUseCase {
    override fun execute(query: GetQuestionPapersQuery): List<QuestionPaperView> =
        questionReader.findPapersByIds(query.questionIds)
}
