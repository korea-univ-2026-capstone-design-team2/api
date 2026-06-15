package com.examhelper.api.exam.port.inbound.query

data class CheckExamExistsQuery(
    val examId: Long,
    val memberId: Long
)
