package com.examhelper.api.question_generation.port.outbound

import com.examhelper.api.question_generation.domain.QuestionGeneration

interface QuestionGenerationStore {
    fun save(questionGeneration: QuestionGeneration)
}
