package com.examhelper.api.exam_attempt.adapter.web.dto.response

import com.examhelper.api.exam_attempt.port.inbound.result.SubmitExamAttemptResult
import java.time.Instant

data class SubmitExamAttemptResDto(
    val attemptId: String,
    val examId: Long,
    val status: String,
    val submittedAt: Instant
) {
    companion object {
        fun from(result: SubmitExamAttemptResult): SubmitExamAttemptResDto {
            return SubmitExamAttemptResDto(
                attemptId = result.attemptId.toString(),
                examId = result.examId,
                status = result.status,
                submittedAt = result.submittedAt,
            )
        }
    }
}
