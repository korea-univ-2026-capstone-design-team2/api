package com.examhelper.api.question_generation.adapter.web.frame

data class SourceDto(
    val exam: String,
    val year: Int,
    val subject: String,
    val questionNumber: Int,
    val page: Int,
)
