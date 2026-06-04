package com.examhelper.api.exam.application

import com.examhelper.api.exam.port.inbound.CheckExamExistsUseCase
import com.examhelper.api.exam.port.inbound.query.CheckExamExistsQuery
import com.examhelper.api.exam.port.inbound.result.CheckExamExistsResult
import com.examhelper.api.exam.port.outbound.ExamReader
import org.springframework.stereotype.Service

@Service
class CheckExamExistsService(
    private val examExistsReader: ExamReader
) : CheckExamExistsUseCase {
    override fun execute(command: CheckExamExistsQuery): CheckExamExistsResult {
        val exists = examExistsReader.existsById(command.examId)
        return CheckExamExistsResult(exists)
    }
}
