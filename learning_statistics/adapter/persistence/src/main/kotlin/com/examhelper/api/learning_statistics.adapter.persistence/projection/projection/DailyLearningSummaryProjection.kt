package com.examhelper.api.learning_statistics.adapter.persistence.projection.projection

interface DailyLearningSummaryProjection {
    val totalQuestions: Long
    val totalCorrect: Long
    val totalStudySeconds: Long
}
