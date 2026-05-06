package com.examhelper.api.question_generation.port.inbound.result

import com.examhelper.api.kernel.identifier.QuestionGenerationId

data class GenerateQuestionResult(
    val questionGenerationId: QuestionGenerationId
)
