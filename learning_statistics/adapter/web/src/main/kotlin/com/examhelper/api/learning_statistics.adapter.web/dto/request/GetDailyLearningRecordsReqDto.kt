package com.examhelper.api.learning_statistics.adapter.web.dto.request

import com.examhelper.api.learning_statistics.port.inbound.query.GetDailyLearningRecordsQuery
import java.time.LocalDate

data class GetDailyLearningRecordsReqDto(
    val from: LocalDate,
    val to: LocalDate
) {
    fun toQuery(memberId: String) = GetDailyLearningRecordsQuery(
        memberId = memberId.toLong(),
        from = from,
        to = to
    )
}
