package com.examhelper.api.learning_statistics.port.inbound.query

import java.time.LocalDate

data class GetDailyLearningSummaryQuery(
    val memberId: Long,
    val from: LocalDate,
    val to: LocalDate
)
