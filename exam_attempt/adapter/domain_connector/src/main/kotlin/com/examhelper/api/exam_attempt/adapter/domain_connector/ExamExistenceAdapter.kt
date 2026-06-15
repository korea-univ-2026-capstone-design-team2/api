package com.examhelper.api.exam_attempt.adapter.domain_connector

import com.examhelper.api.exam.port.inbound.CheckExamExistsUseCase
import com.examhelper.api.exam.port.inbound.query.CheckExamExistsQuery
import com.examhelper.api.exam_attempt.port.outbound.ExamExistencePort
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId
import org.springframework.stereotype.Component

@Component
class ExamExistenceAdapter(
    private val checkExamExistsUseCase: CheckExamExistsUseCase
) : ExamExistencePort {
    override fun existsById(examId: ExamId, memberId: MemberId): Boolean =
        checkExamExistsUseCase.execute(CheckExamExistsQuery(
            examId.value,
            memberId.value,
        )).exists
}
