package com.examhelper.api.exam.port.inbound.view

data class ExamItemView(
    val examItemId: Long,
    val questionId: Long,
    val ordering: Int
)
