package com.examhelper.api.learning_statistics.adapter.web.dto.request

import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningSummaryQuery
import java.time.LocalDate

data class GetDailyLearningSummaryReqDto(
    val from: LocalDate,
    val to: LocalDate
) {
    fun toQuery(memberId: Long) = GetDailyLearningSummaryQuery(
        memberId = memberId,
        from = from,
        to = to
    )
}