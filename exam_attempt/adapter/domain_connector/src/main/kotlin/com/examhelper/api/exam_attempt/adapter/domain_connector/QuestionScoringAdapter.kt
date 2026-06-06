package com.examhelper.api.exam_attempt.adapter.domain_connector

import com.examhelper.api.exam_attempt.port.outbound.QuestionScoringPort
import com.examhelper.api.exam_attempt.port.outbound.result.QuestionScoringResult
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.question.port.inbound.GetCorrectAnswersUseCase
import com.examhelper.api.question.port.inbound.query.GetCorrectAnswersQuery
import org.springframework.stereotype.Component

@Component
class QuestionScoringAdapter(
    private val getCorrectAnswersUseCase: GetCorrectAnswersUseCase
) : QuestionScoringPort {
    override fun findAnswerSheets(questionItemIds: List<QuestionItemId>): List<QuestionScoringResult> {
        val result = getCorrectAnswersUseCase.execute(GetCorrectAnswersQuery(questionItemIds.map { it.value }))

        return result.map { QuestionScoringResult(
            QuestionItemId(it.questionItemId),
            it.correctNumber
        ) }
    }
}
