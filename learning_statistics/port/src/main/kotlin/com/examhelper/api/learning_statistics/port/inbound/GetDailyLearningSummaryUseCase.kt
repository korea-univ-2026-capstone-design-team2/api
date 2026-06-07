package com.examhelper.api.learning_statistics.port.inbound

import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningSummaryQuery
import com.examhelper.api.learning_statistics.port.inbound.result.DailyLearningSummaryResult

interface GetDailyLearningSummaryUseCase {
    fun execute(query: GetDailyLearningSummaryQuery): DailyLearningSummaryResult
}