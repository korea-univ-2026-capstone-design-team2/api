package com.examhelper.api.exam.port.inbound

import com.examhelper.api.exam.port.inbound.query.GetExamDetailQuery
import com.examhelper.api.exam.port.inbound.view.ExamDetailView

interface GetExamDetailUseCase {
    fun execute(query: GetExamDetailQuery): ExamDetailView
}
