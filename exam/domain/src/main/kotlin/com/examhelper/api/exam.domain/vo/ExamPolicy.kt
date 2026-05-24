package com.examhelper.api.exam.domain.vo

data class ExamPolicy(
    val shuffleQuestions: Boolean,
    val shuffleChoices: Boolean,
    val allowRetake: Boolean,
)
