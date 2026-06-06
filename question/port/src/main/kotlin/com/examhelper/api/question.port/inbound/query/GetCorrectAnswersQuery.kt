package com.examhelper.api.question.port.inbound.query

data class GetCorrectAnswersQuery(
    val questionItemIds: List<Long>
)
