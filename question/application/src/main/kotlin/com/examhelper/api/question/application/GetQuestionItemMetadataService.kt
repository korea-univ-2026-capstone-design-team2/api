package com.examhelper.api.question.application

import com.examhelper.api.question.port.inbound.GetQuestionItemMetadataUseCase
import com.examhelper.api.question.port.inbound.query.GetQuestionItemMetadataQuery
import com.examhelper.api.question.port.inbound.view.QuestionItemMetadataView
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetQuestionItemMetadataService(
    private val questionReader: QuestionReader
) : GetQuestionItemMetadataUseCase {
    @Transactional(readOnly = true)
    override fun execute(query: GetQuestionItemMetadataQuery): List<QuestionItemMetadataView> =
        questionReader.findItemSummaries(query.questionItemIds)
}
