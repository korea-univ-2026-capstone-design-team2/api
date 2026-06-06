package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.query.GetCorrectAnswersQuery
import com.examhelper.api.question.port.inbound.view.CorrectAnswerView

interface GetCorrectAnswersUseCase {
    fun execute(query: GetCorrectAnswersQuery): List<CorrectAnswerView>
}
