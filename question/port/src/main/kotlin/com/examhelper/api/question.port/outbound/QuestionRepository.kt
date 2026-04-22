package com.examhelper.api.question.port.outbound

import com.examhelper.api.question.domain.Question

interface QuestionRepository {
    fun save(question: Question)
}
