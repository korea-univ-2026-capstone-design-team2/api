package com.examhelper.api.exam_attempt.port.inbound.result

import java.time.Instant

data class SaveExamAttemptAnswersResult(
    val attemptId: Long,
    val savedCount: Int,
    val updatedAt: Instant
)
