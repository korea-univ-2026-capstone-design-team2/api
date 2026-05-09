package com.examhelper.api.question_generation.adapter.web.frame

data class ProblemDto(
    val stem: String,
    val passage: PassageDto?,
    val choices: List<ChoiceDto>,
    val answer: Int,
    val explanation: ExplanationDto,
)
