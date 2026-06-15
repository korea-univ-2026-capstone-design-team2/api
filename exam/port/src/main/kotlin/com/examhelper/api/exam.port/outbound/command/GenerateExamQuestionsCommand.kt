package com.examhelper.api.exam.port.outbound.command

import com.examhelper.api.exam.domain.vo.ExamMetadata
import com.examhelper.api.kernel.identifier.MemberId

data class GenerateExamQuestionsCommand(
    val memberId: MemberId,
    val metadata: ExamMetadata,
    val frameSearchTopK: Int = 3
)
