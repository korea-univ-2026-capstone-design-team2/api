package com.examhelper.api.question.port.inbound.query

data class GetQuestionPaperQuery(
    val questionId: Long,
    val memberId: Long
)
