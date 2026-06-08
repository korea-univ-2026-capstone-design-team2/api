package com.examhelper.api.exam_attempt.adapter.web.dto.response

import com.examhelper.api.exam_attempt.port.inbound.result.SaveExamAttemptAnswersResult
import java.time.Instant

data class SaveExamAttemptAnswersResDto(
    val attemptId: String,
    val savedCount: Int,
    val updatedAt: Instant
) {
    companion object {
        fun fromResult(result: SaveExamAttemptAnswersResult): SaveExamAttemptAnswersResDto {
            return SaveExamAttemptAnswersResDto(
                attemptId = result.attemptId.toString(),
                savedCount = result.savedCount,
                updatedAt = result.updatedAt
            )
        }
    }
}
