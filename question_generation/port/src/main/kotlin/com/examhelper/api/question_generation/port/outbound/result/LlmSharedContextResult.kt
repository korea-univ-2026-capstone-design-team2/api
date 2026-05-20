package com.examhelper.api.question_generation.port.outbound.result

data class LlmSharedContextResult(
    val content: String,
    val description: String?
)
