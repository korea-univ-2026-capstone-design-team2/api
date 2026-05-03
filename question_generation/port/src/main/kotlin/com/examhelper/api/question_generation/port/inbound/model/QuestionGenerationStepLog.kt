package com.examhelper.api.question_generation.port.inbound.model

import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionGenerationStepLogId
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStep
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStepStatus
import java.time.Instant

data class QuestionGenerationStepLog(
    val id: QuestionGenerationStepLogId,
    val generationId : QuestionGenerationId,
    val step         : QuestionGenerationStep,
    val status       : QuestionGenerationStepStatus,
    val durationMs   : Long?,
    val detail       : String?,
    val occurredAt   : Instant,
)
