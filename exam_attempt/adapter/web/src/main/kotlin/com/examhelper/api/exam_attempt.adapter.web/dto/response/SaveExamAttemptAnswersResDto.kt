package com.examhelper.api.exam_attempt.adapter.web.dto.response

import java.time.Instant

data class SaveExamAttemptAnswersResDto(
    val attemptId: Long,
    val savedCount: Int,
    val updatedAt: Instant
)
