package com.examhelper.api.question.port.inbound.command

import com.examhelper.api.question.domain.vo.QualityScore

data class AssignQualityScoreCommand(
    val questionId: Long,
    val score: QualityScore
)
