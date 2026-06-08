package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.question.port.inbound.query.GetQuestionPapersQuery

data class GetQuestionPapersReqDto(
    val questionIds: List<String>
) {
    fun toQuery(): GetQuestionPapersQuery = GetQuestionPapersQuery(
        questionIds.map { it.toLong() }
    )
}
