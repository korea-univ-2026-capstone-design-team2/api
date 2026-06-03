package com.examhelper.api.question_generation.port.outbound

interface QuestionGenerationMetricsPort {
    fun recordTotalDuration(
        durationMs: Long
    )
}
