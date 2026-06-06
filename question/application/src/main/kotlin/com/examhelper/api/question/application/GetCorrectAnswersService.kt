package com.examhelper.api.question.application

import com.examhelper.api.question.port.inbound.GetCorrectAnswersUseCase
import com.examhelper.api.question.port.inbound.query.GetCorrectAnswersQuery
import com.examhelper.api.question.port.inbound.view.CorrectAnswerView
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service

@Service
class GetCorrectAnswersService(
    private val questionReader: QuestionReader
) : GetCorrectAnswersUseCase {
    override fun execute(query: GetCorrectAnswersQuery): List<CorrectAnswerView> {
        return questionReader.findCorrectAnswersByQuestionItemIds(query.questionItemIds)
    }
}
