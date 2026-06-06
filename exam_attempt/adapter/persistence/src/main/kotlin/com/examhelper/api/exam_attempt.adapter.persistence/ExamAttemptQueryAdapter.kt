package com.examhelper.api.exam_attempt.adapter.persistence

import com.examhelper.api.exam_attempt.adapter.persistence.projection.ExamAttemptAnswerResultProjection
import com.examhelper.api.exam_attempt.adapter.persistence.projection.ExamAttemptResultProjection
import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptAnswerResultView
import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptResultSummaryView
import com.examhelper.api.exam_attempt.port.outbound.ExamAttemptReader
import org.springframework.stereotype.Repository

@Repository
class ExamAttemptQueryAdapter(
    private val attemptJpaReader: ExamAttemptJpaReader,
    private val answerJpaReader: ExamAttemptAnswerJpaReader,
) : ExamAttemptReader {
    override fun findResultAttempt(attemptId: Long): ExamAttemptResultSummaryView? =
        attemptJpaReader.findResultAttempt(attemptId)?.toView()

    override fun findResultAnswers(attemptId: Long): List<ExamAttemptAnswerResultView> =
        answerJpaReader.findResultAnswers(attemptId).map { it.toView() }
}

private fun ExamAttemptResultProjection.toView() =
    ExamAttemptResultSummaryView(
        attemptId = attemptId,
        examId = examId,
        memberId = memberId,
        status = status,
        startedAt = startedAt,
        submittedAt = submittedAt,
    )

private fun ExamAttemptAnswerResultProjection.toView() =
    ExamAttemptAnswerResultView(
        questionItemId = questionItemId,
        selectedNumber = selectedNumber,
        timeSpentSeconds = timeSpentSeconds,
        markedUnknown = markedUnknown,
        bookmarked = bookmarked,
    )
