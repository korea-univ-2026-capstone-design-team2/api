package com.examhelper.api.question.port.inbound.query

data class GetQuestionReviewsQuery(
    val questionIds: List<Long>,
    val memberId: Long
)
