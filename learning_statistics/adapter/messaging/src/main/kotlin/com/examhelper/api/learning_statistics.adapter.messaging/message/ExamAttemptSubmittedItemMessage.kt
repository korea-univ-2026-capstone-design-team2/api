package com.examhelper.api.learning_statistics.adapter.messaging.message

data class ExamAttemptSubmittedItemMessage(
    val questionItemId: Long,
    val subject: String,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int
)
