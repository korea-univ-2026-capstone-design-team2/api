package com.examhelper.api.question_generation.port.outbound.result

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionItemId

data class QuestionCreationResult(
    val questionId: QuestionId,
    val questionItemIds: List<QuestionItemId>,
)
