package com.examhelper.api.question.port.inbound.query

data class GetQuestionPapersQuery(
    val questionIds: List<Long>,
    val memberId: Long
)
