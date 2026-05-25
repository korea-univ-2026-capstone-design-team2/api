package com.examhelper.api.exam.port.outbound

import com.examhelper.api.exam.port.outbound.command.GenerateExamQuestionsCommand
import com.examhelper.api.exam.port.outbound.result.GenerateExamQuestionsResult

interface GenerateExamQuestionsPort {
    fun generate(command: GenerateExamQuestionsCommand): GenerateExamQuestionsResult
}
