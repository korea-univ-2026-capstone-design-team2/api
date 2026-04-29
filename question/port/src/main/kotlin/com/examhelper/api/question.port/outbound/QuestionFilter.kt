package com.examhelper.api.question.port.outbound

import com.examhelper.api.question.domain.type.DifficultyLevel
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.type.QuestionType
import com.examhelper.api.question.domain.type.Subject

data class QuestionFilter(
    val subject: Subject? = null,
    val questionType: QuestionType? = null,
    val difficulty: DifficultyLevel? = null,
    val status: QuestionStatus? = null,
    val page: Int = 0,
    val size: Int = 20,
) {
    companion object {
        const val DEFAULT_PAGE = 0
        const val DEFAULT_SIZE = 20
        const val MAX_SIZE = 100
    }

    init {
        require(page >= 0) { "Page must be >= 0" }
        require(size in 1..MAX_SIZE) { "Size must be 1..$MAX_SIZE" }
    }
}
