package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionAssertionException

sealed class SharedQuestionContext {
    data class Text(
        val content: String,
        val description: String?,
    ) : SharedQuestionContext() {
        init {
            require(content.isNotBlank()) {
                throw QuestionAssertionException.SharedContextContentBlank()
            }
        }
    }
}
