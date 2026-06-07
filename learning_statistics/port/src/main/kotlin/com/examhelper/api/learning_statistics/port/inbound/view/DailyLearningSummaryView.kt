package com.examhelper.api.learning_statistics.port.inbound.view

data class DailyLearningSummaryView(
    val totalQuestions: Long,
    val totalCorrect: Long,
    val totalStudySeconds: Long
)
