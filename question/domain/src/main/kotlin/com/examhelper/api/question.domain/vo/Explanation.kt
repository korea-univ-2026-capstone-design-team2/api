package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionItemAssertionException

data class Explanation(
    val correctReason: String,
    val incorrectReasons: Map<Int, String> = emptyMap(),
) {
    init {
        require(correctReason.isNotBlank()) {
            throw QuestionItemAssertionException.ExplanationCorrectReasonBlank()
        }
    }
}
