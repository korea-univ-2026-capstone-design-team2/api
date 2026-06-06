package com.examhelper.api.exam_attempt.adapter.persistence.projection

import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import java.time.Instant

data class ExamAttemptResultProjection(
    val attemptId: Long,
    val examId: Long,
    val memberId: Long,
    val status: ExamAttemptStatus,
    val startedAt: Instant,
    val submittedAt: Instant?
)
