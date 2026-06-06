package com.examhelper.api.exam_attempt.port.inbound.view

import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import java.time.Instant

data class ExamAttemptResultView(
    val attemptId: Long,
    val examId: Long,
    val status: ExamAttemptStatus,
    val totalCount: Int,
    val correctCount: Int,
    val score: Double,
    val accuracy: Double,
    val timeSpentSeconds: Int,
    val submittedAt: Instant,
    val items: List<ExamAttemptResultItemView>
)
