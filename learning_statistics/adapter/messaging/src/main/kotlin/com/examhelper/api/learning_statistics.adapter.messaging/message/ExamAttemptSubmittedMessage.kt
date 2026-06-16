package com.examhelper.api.learning_statistics.adapter.messaging.message

import java.time.Instant

data class ExamAttemptSubmittedMessage(
    val attemptId: Long,
    val examId: Long,
    val memberId: Long,
    val items: List<ExamAttemptSubmittedItemMessage>,
    val submittedAt: Instant
)
