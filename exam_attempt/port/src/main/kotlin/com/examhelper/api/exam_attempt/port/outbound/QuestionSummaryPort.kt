package com.examhelper.api.exam_attempt.port.outbound

import com.examhelper.api.exam_attempt.port.outbound.result.QuestionSummaryResult
import com.examhelper.api.kernel.identifier.QuestionItemId

interface QuestionSummaryPort {
    fun findSummaries(questionIds: List<QuestionItemId>): List<QuestionSummaryResult>
}
