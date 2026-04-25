package com.examhelper.api.question.port.inbound.result

data class AssignQualityScoreResult(
    val questionId: Long,
    val score: Double,
    val isPassing: Boolean
)
