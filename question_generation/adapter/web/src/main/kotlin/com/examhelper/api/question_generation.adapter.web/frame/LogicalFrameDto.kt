package com.examhelper.api.question_generation.adapter.web.frame

data class LogicalFrameDto(
    val reasoningType: String,
    val premises: List<String>,
    val conditions: List<String>,
    val goal: String,
    val inferenceStructure: List<String>,
)
