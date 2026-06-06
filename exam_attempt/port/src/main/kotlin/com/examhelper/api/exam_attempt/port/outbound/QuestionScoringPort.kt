package com.examhelper.api.exam_attempt.port.outbound

import com.examhelper.api.exam_attempt.port.outbound.result.QuestionScoringResult
import com.examhelper.api.kernel.identifier.QuestionItemId

interface QuestionScoringPort {
    fun findAnswerSheets(questionItemIds: List<QuestionItemId>): List<QuestionScoringResult>
}
