package com.examhelper.api.question.application

import com.examhelper.api.exam.port.inbound.GetExamListUseCase
import com.examhelper.api.exam.port.inbound.query.ExamFilter
import com.examhelper.api.exam.port.inbound.query.GetExamListResult
import com.examhelper.api.exam.port.outbound.ExamReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetExamListService(
    private val examReader: ExamReader,
) : GetExamListUseCase {
    override fun execute(filter: ExamFilter): GetExamListResult =
        GetExamListResult(
            items = examReader.findSummaries(filter),
            totalCount = examReader.count(filter),
            page = filter.page,
            size = filter.size,
        )
}
