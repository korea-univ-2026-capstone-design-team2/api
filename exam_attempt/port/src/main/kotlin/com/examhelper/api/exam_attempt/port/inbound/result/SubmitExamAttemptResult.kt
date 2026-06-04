package com.examhelper.api.exam_attempt.port.inbound.result

import java.time.Instant

data class SubmitExamAttemptResult(
    val attemptId: Long,
    val examId: Long,
    val status: String,
    val submittedAt: Instant
)
