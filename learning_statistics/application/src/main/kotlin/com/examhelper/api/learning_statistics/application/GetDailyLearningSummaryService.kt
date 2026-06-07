package com.examhelper.api.learning_statistics.application

import com.examhelper.api.learning_statistics.port.inbound.GetDailyLearningSummaryUseCase
import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningSummaryQuery
import com.examhelper.api.learning_statistics.port.inbound.result.DailyLearningSummaryResult
import com.examhelper.api.learning_statistics.port.outbound.DailyLearningStatReader
import org.springframework.stereotype.Service

@Service
class GetDailyLearningSummaryService(
    private val dailyLearningStatReader: DailyLearningStatReader,
) : GetDailyLearningSummaryUseCase {
    override fun execute(
        query: GetDailyLearningSummaryQuery,
    ): DailyLearningSummaryResult {
        val summary = dailyLearningStatReader.findSummary(
                memberId = query.memberId,
                from = query.from,
                to = query.to,
            )

        if (summary == null) {
            return DailyLearningSummaryResult(
                totalQuestions = 0,
                totalCorrect = 0,
                totalStudySeconds = 0,
                accuracy = 0.0,
            )
        }

        return DailyLearningSummaryResult(
            totalQuestions = summary.totalQuestions,
            totalCorrect = summary.totalCorrect,
            totalStudySeconds = summary.totalStudySeconds,
            accuracy =
                if (summary.totalQuestions == 0L) 0.0
                else summary.totalCorrect.toDouble() / summary.totalQuestions
        )
    }
}
