package com.examhelper.api.exam.adapter.messaging.message

data class QuestionGenerationCompletedMessage(
    val generationId: Long,
    val successCount: Int,
    val failureCount: Int
)
