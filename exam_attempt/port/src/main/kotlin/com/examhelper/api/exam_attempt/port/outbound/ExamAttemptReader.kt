package com.examhelper.api.exam_attempt.port.outbound

import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptAnswerResultView
import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptResultSummaryView

interface ExamAttemptReader {
    fun findResultAttempt(attemptId: Long): ExamAttemptResultSummaryView?
    fun findResultAnswers(attemptId: Long): List<ExamAttemptAnswerResultView>
}
