package com.examhelper.api.exam.port.inbound.result

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.QuestionGenerationId

data class GenerateExamResult(
    val examId: ExamId,
    val status: ExamStatus,
    val successCount: Int,
    val failCount: Int,
    val generationId: QuestionGenerationId?
)
