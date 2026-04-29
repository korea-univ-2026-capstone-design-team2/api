package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionAssertionException

sealed class AnswerChoice {
    abstract val number: Int
    abstract val isCorrect: Boolean

    data class TextChoice(
        override val number: Int,
        val content: String,
        override val isCorrect: Boolean,
    ) : AnswerChoice() {
        init { validate() }

        private fun validate() {
            require(number in 1..5) {
                throw QuestionAssertionException.ChoiceNumberOutOfRange(number)
            }
            require(content.isNotBlank()) {
                throw QuestionAssertionException.ChoiceContentBlank()
            }
        }
    }

    data class PropositionCombinationChoice(
        override val number: Int,
        val labels: List<PropositionLabel>,
        override val isCorrect: Boolean,
    ) : AnswerChoice() {
        init { validate() }

        private fun validate() {
            require(number in 1..5) {
                throw QuestionAssertionException.ChoiceNumberOutOfRange(number)
            }
            require(labels.isNotEmpty()) {
                throw QuestionAssertionException.PropositionLabelEmpty()
            }
        }
    }
}
