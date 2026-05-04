package com.examhelper.api.question_generation.port.outbound.result

data class LlmPassageResult(
    val content     : String,
    val description : String?,
)
