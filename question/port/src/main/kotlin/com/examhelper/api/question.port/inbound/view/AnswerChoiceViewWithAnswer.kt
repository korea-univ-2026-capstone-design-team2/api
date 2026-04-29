package com.examhelper.api.question.port.inbound.view

data class AnswerChoiceViewWithAnswer(
    val number: Int,
    val text: String,
    val isCorrect: Boolean
)
