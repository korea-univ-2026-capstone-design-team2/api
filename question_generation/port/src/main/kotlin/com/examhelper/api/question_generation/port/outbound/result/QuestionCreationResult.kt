package com.examhelper.api.question_generation.port.outbound.result

import com.examhelper.api.kernel.identifier.QuestionId

data class QuestionCreationResult(
    val questionId: QuestionId
)
