package com.examhelper.api.exam.port.inbound

import com.examhelper.api.exam.port.inbound.query.ExamFilter
import com.examhelper.api.exam.port.inbound.query.GetExamListResult

interface GetExamListUseCase {
    fun execute(filter: ExamFilter): GetExamListResult
}
