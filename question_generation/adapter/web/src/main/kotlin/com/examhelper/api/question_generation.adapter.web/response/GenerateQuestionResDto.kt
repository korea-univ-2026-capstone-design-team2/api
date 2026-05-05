package com.examhelper.api.question_generation.adapter.web.response

import com.examhelper.api.question_generation.domain.type.QuestionGenerationStatus

data class GenerateQuestionResDto(
    val generationId: Long,
    val status: String = QuestionGenerationStatus.COMPLETED.name,
)
