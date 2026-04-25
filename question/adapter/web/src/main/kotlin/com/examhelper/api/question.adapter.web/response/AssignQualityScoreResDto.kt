package com.examhelper.api.question.adapter.web.response

import com.examhelper.api.question.port.inbound.result.AssignQualityScoreResult

data class AssignQualityScoreResDto(
    val questionId: Long,
    val score: Double,
    val isPassing: Boolean
) {
    companion object {
        fun fromResult(result: AssignQualityScoreResult) = AssignQualityScoreResDto(
            questionId = result.questionId,
            score = result.score,
            isPassing = result.isPassing,
        )
    }
}
