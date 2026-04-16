package com.examhelper.api.question.port.out

import com.examhelper.api.question.domain.Question

interface QuestionRepository {
    fun save(question: Question)
}
