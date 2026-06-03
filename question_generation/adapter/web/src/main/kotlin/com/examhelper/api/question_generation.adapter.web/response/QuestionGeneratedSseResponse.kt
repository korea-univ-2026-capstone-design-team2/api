package com.examhelper.api.question_generation.adapter.web.response

data class QuestionGeneratedSseResponse(
    val generationId: Long,
    val questionId: Long
)
