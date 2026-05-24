package com.examhelper.api.exam.port.inbound.query

import com.examhelper.api.exam.port.inbound.view.ExamSummaryView

data class GetExamListResult(
    val items: List<ExamSummaryView>,
    val totalCount: Long,
    val page: Int,
    val size: Int,
)
