package com.examhelper.api.exam_attempt.port.inbound

import com.examhelper.api.exam_attempt.port.inbound.query.GetExamAttemptResultQuery
import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptResultView

interface GetExamAttemptResultUseCase {
    fun execute(query: GetExamAttemptResultQuery): ExamAttemptResultView
}
