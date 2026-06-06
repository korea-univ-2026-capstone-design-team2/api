package com.examhelper.api.question.port.inbound.view

data class CorrectAnswerView(
    val questionItemId: Long,
    val correctNumber: Int,
)
