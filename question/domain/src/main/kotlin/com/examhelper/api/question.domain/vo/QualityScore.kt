package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionItemAssertionException

@JvmInline
value class QualityScore(val value: Double) {
    init {
        require(value in 0.0..1.0) {
            throw QuestionItemAssertionException.QualityScoreOutOfRange(value)
        }
    }

    fun isPassing(): Boolean = value >= PASSING_THRESHOLD

    companion object {
        private const val PASSING_THRESHOLD = 0.75
    }
}
