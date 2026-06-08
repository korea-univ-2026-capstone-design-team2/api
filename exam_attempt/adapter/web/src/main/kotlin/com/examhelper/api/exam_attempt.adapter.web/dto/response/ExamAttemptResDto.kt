package com.examhelper.api.exam_attempt.adapter.web.dto.response

import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptResultView
import java.time.Instant

data class ExamAttemptResDto(
    val attemptId: String,
    val examId: String,
    val status: ExamAttemptStatus,
    val totalCount: Int,
    val correctCount: Int,
    val score: Double,
    val accuracy: Double,
    val timeSpentSeconds: Int,
    val submittedAt: Instant,
    val items: List<ExamAttemptResultItemResponse>
) {
    companion object {
        fun fromView(view: ExamAttemptResultView): ExamAttemptResDto =
            ExamAttemptResDto(
                attemptId = view.attemptId.toString(),
                examId = view.examId.toString(),
                status = view.status,
                totalCount = view.totalCount,
                correctCount = view.correctCount,
                score = view.score,
                accuracy = view.accuracy,
                timeSpentSeconds = view.timeSpentSeconds,
                submittedAt = view.submittedAt,
                items = view.items.map(ExamAttemptResultItemResponse::fromView)
            )
    }
}
