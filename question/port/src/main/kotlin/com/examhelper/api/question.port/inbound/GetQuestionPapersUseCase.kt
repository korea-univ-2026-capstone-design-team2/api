package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.query.GetQuestionPapersQuery
import com.examhelper.api.question.port.inbound.view.QuestionPaperView

interface GetQuestionPapersUseCase {
    fun execute(query: GetQuestionPapersQuery): List<QuestionPaperView>
}
