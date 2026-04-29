package com.examhelper.api.question.port.outbound

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.Question

interface QuestionStore {
    fun save(question: Question)
    fun loadById(id: QuestionId): Question?
}
