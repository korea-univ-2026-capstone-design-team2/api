package com.examhelper.api.exam.port.inbound

import com.examhelper.api.exam.port.inbound.query.CheckExamExistsQuery
import com.examhelper.api.exam.port.inbound.result.CheckExamExistsResult

interface CheckExamExistsUseCase {
    fun execute(command: CheckExamExistsQuery): CheckExamExistsResult
}
