package com.examhelper.api.exam_attempt.adapter.web.dto.response

import com.examhelper.api.exam_attempt.port.inbound.result.StartExamAttemptResult
import java.time.Instant

data class StartExamAttemptResDto(
    val attemptId: Long,
    val examId: Long,
    val status: String,
    val startedAt: Instant
) {
    companion object {
        fun fromResult(result: StartExamAttemptResult): StartExamAttemptResDto {
            return StartExamAttemptResDto(
                attemptId = result.attemptId,
                examId = result.examId,
                status = result.status,
                startedAt = result.startedAt
            )
        }
    }
}
