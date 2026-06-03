package com.examhelper.api.exam.port.outbound.command

import com.examhelper.api.exam.domain.vo.ExamMetadata

data class GenerateExamQuestionsCommand(
    val metadata: ExamMetadata,
    val frameSearchTopK: Int = 3
)
