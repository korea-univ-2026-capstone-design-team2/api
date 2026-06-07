package com.examhelper.api.learning_statistics.port.inbound.result

data class DailyLearningSummaryResult(
    val totalQuestions: Long,
    val totalCorrect: Long,
    val totalStudySeconds: Long,
    val accuracy: Double
)
