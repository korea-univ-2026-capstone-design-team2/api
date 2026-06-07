package com.examhelper.api.learning_statistics.adapter.web.dto.response

import com.examhelper.api.learning_statistics.port.inbound.result.DailyLearningSummaryResult

data class GetDailyLearningSummaryResDto(
    val totalQuestions: Long,
    val totalCorrect: Long,
    val totalStudySeconds: Long,
    val accuracy: Double,
) {
    companion object {
        fun from(
            result: DailyLearningSummaryResult,
        ) = GetDailyLearningSummaryResDto(
            totalQuestions = result.totalQuestions,
            totalCorrect = result.totalCorrect,
            totalStudySeconds = result.totalStudySeconds,
            accuracy = result.accuracy
        )
    }
}