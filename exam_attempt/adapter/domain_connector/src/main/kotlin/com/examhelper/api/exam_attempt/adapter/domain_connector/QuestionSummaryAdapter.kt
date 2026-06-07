package com.examhelper.api.exam_attempt.adapter.domain_connector

import com.examhelper.api.exam_attempt.port.outbound.QuestionSummaryPort
import com.examhelper.api.exam_attempt.port.outbound.result.QuestionSummaryResult
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.question.port.inbound.GetQuestionItemMetadataUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionItemMetadataQuery
import org.springframework.stereotype.Component

@Component
class QuestionSummaryAdapter(
    private val getQuestionSummaryUseCase: GetQuestionItemMetadataUseCase
) : QuestionSummaryPort {
    override fun findSummaries(questionIds: List<QuestionItemId>): List<QuestionSummaryResult> {
        val views = getQuestionSummaryUseCase.execute(GetQuestionItemMetadataQuery(questionIds.map { it.value }))

        return views.map { QuestionSummaryResult(
            questionItemId = it.questionItemId,
            generationId = it.generationId,
            subject = it.subject,
            questionType = it.questionType,
            difficulty = it.difficulty
        ) }
    }
}
