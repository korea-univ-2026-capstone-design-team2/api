package com.examhelper.api.exam.port.inbound.query

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject

data class ExamFilter(
    val subject: Subject?,
    val questionType: QuestionType?,
    val difficulty: DifficultyLevel?,
    val status: ExamStatus?,
    val page: Int = 0,
    val size: Int = 20,
) {
    init {
        require(page >= 0) { "page must be >= 0" }
        require(size in 1..100) { "size must be between 1 and 100" }
    }
}
