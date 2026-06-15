package com.examhelper.api.question.port.outbound

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class QuestionFilter(
    val memberId: Long,
    val subject: Subject?,
    val questionType: QuestionType?,
    val difficulty: DifficultyLevel?,
    val page: Int,
    val size: Int
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
