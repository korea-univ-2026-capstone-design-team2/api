package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionAssertionException

sealed class Passage {
    data class TextPassage(
        val content: String,
        val description: String? = null,
    ) : Passage() {
        init {
            if (content.isBlank()) throw QuestionAssertionException.PassageContentBlank()
        }
    }

    // 확장 시 추가 예정
    // data class CodePassage(val code: String, val language: String, ...) : Passage()
    // data class ImagePassage(val url: String, val altText: String, ...) : Passage()
}
