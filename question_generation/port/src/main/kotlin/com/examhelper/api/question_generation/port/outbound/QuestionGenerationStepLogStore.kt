package com.examhelper.api.question_generation.port.outbound

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.question_generation.port.inbound.model.QuestionGenerationStepLog

interface QuestionGenerationStepLogStore {
    fun save(log: QuestionGenerationStepLog)
    fun loadAllByGenerationId(
        generationId: QuestionGenerationId,
    ): List<QuestionGenerationStepLog>
}
