package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.question.domain.vo.Explanation

data class ExplanationReqDto(
    val correctReason: String,
    val incorrectReasons: Map<Int, String>,
) {
    fun toDomain(): Explanation =
        Explanation(
            correctReason = correctReason,
            incorrectReasons = incorrectReasons,
        )
}
