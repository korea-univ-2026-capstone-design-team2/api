package com.examhelper.api.exam.application

import com.examhelper.api.exam.domain.exception.ExamException
import com.examhelper.api.exam.port.inbound.GetExamDetailUseCase
import com.examhelper.api.exam.port.inbound.query.GetExamDetailQuery
import com.examhelper.api.exam.port.inbound.view.ExamDetailView
import com.examhelper.api.exam.port.outbound.ExamReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetExamDetailService(
    private val examReader: ExamReader
) : GetExamDetailUseCase {
    override fun execute(query: GetExamDetailQuery): ExamDetailView =
        examReader.findDetailById(query.examId, query.memberId)
            ?: throw ExamException.NotFound(query.examId)
}
