package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.query.GetQuestionItemMetadataQuery
import com.examhelper.api.question.port.inbound.view.QuestionItemMetadataView

interface GetQuestionItemMetadataUseCase {
    fun execute(query: GetQuestionItemMetadataQuery): List<QuestionItemMetadataView>
}
