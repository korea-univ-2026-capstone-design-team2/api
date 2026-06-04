package com.examhelper.api.exam_attempt.port.inbound.result

import java.time.Instant

data class StartExamAttemptResult(
    val attemptId: Long,
    val examId: Long,
    val status: String,
    val startedAt: Instant
)
