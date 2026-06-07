package com.examhelper.api.learning_statistics.port.inbound

import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningRecordsQuery
import com.examhelper.api.learning_statistics.port.inbound.result.DailyLearningRecordResult

interface GetDailyLearningRecordsUseCase {
    fun execute(query: GetDailyLearningRecordsQuery): List<DailyLearningRecordResult>
}
