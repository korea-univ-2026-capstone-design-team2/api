package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionAssertionException

sealed class AnswerSheet {

    data class MultipleChoiceSheet(
        val choices: List<AnswerChoice>,
        val correctNumber: Int,
    ) : AnswerSheet() {
        init { validate() }

        private fun validate() {
            validateChoiceSize()
            validateChoiceNumberUniqueness()
            validateCorrectNumber()
            validateCorrectAnswerCount()
            validateCorrectNumberChoiceConsistency()
        }

        private fun validateChoiceSize() {
            require(choices.size == 5) {
                throw QuestionAssertionException.ChoiceSizeMismatch(choices.size)
            }
        }

        private fun validateChoiceNumberUniqueness() {
            val numbers = choices.map { it.number }
            require(numbers.distinct().size == numbers.size) {
                throw QuestionAssertionException.ChoiceNumberDuplicated()
            }
        }

        private fun validateCorrectNumber() {
            require(correctNumber in 1..5) {
                throw QuestionAssertionException.CorrectNumberOutOfRange(correctNumber)
            }
        }

        private fun validateCorrectAnswerCount() {
            val correctCount = choices.count { it.isCorrect }
            require(correctCount == 1) {
                throw QuestionAssertionException.CorrectAnswerCountMismatch(correctCount)
            }
        }

        private fun validateCorrectNumberChoiceConsistency() {
            val markedChoice = choices.find { it.number == correctNumber }
            require(markedChoice != null && markedChoice.isCorrect) {
                throw QuestionAssertionException.CorrectNumberChoiceMismatch(correctNumber)
            }
        }
    }

    // 확장 시 추가 예정
    // data class ShortAnswerSheet(val correctAnswer: String) : AnswerSheet()
}
