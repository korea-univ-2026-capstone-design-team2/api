package com.examhelper.api.exam_attempt.port.outbound.result

import com.examhelper.api.kernel.identifier.QuestionItemId

data class QuestionScoringResult(
    val questionItemId: QuestionItemId,
    val correctNumber: Int
)
