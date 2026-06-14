package com.examhelper.api.exam.adapter.messaging.message

data class QuestionGeneratedMessage(
    val generationId: Long,
    val questionId: Long,
    val ordering: Int
)
