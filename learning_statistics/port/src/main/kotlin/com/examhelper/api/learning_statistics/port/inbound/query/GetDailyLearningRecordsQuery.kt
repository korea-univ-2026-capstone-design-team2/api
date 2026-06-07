package com.examhelper.api.learning_statistics.port.inbound.query

import java.time.LocalDate

data class GetDailyLearningRecordsQuery(
    val memberId: Long,
    val from: LocalDate,
    val to: LocalDate
)
