package com.examhelper.api.kernel.event

data class ExamAttemptSubmittedItem(
    val questionItemId: Long,
    val subject: String,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int
)
