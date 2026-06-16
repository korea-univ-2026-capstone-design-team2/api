package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.question.port.inbound.query.GetQuestionPapersQuery

data class GetQuestionPapersReqDto(
    val questionIds: List<String>
) {
    fun toQuery(memberId: Long): GetQuestionPapersQuery = GetQuestionPapersQuery(
        questionIds.map { it.toLong() },
        memberId
    )
}
