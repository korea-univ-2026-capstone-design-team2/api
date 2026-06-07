package com.examhelper.api.learning_statistics.port.outbound

import com.examhelper.api.learning_statistics.port.inbound.view.DailyLearningRecordView
import com.examhelper.api.learning_statistics.port.inbound.view.DailyLearningSummaryView
import java.time.LocalDate

interface DailyLearningStatReader {
    fun findSummary(
        memberId: Long,
        from: LocalDate,
        to: LocalDate,
    ): DailyLearningSummaryView?

    fun findDailyRecords(
        memberId: Long,
        from: LocalDate,
        to: LocalDate,
    ): List<DailyLearningRecordView>
}
