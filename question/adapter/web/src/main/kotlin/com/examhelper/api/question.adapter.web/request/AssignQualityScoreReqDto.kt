package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.question.domain.vo.QualityScore
import com.examhelper.api.question.port.inbound.command.AssignQualityScoreCommand

data class AssignQualityScoreReqDto(
    val score: Double
) {
    fun toCommand(questionId: Long) = AssignQualityScoreCommand(
        questionId = questionId,
        score = QualityScore(score),
    )
}
