package com.examhelper.api.question_generation.adapter.web.frame

data class GenerationConstraintsDto(
    val mustPreserve: List<String>,
    val variableElements: List<String>
)
