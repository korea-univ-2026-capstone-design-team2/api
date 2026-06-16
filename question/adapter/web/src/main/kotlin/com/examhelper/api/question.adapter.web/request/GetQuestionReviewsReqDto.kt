package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.question.port.inbound.query.GetQuestionReviewsQuery

data class GetQuestionReviewsReqDto(
    val questionIds: List<String>
) {
    fun toQuery(memberId: Long): GetQuestionReviewsQuery = GetQuestionReviewsQuery(
        questionIds.map { it.toLong() },
        memberId
    )
}
