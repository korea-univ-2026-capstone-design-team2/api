package com.examhelper.api.exam_attempt.application

import com.examhelper.api.exam_attempt.domain.exception.ExamAttemptException
import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import com.examhelper.api.exam_attempt.port.inbound.GetExamAttemptResultUseCase
import com.examhelper.api.exam_attempt.port.inbound.query.GetExamAttemptResultQuery
import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptResultItemView
import com.examhelper.api.exam_attempt.port.inbound.view.ExamAttemptResultView
import com.examhelper.api.exam_attempt.port.outbound.ExamAttemptReader
import com.examhelper.api.exam_attempt.port.outbound.QuestionScoringPort
import com.examhelper.api.kernel.identifier.QuestionItemId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetExamAttemptResultService(
    private val examAttemptReader: ExamAttemptReader,
    private val questionScoringPort: QuestionScoringPort
) : GetExamAttemptResultUseCase {
    @Transactional(readOnly = true)
    override fun execute(query: GetExamAttemptResultQuery): ExamAttemptResultView {
        val attempt = examAttemptReader.findResultAttempt(query.attemptId)
            ?: throw ExamAttemptException.NotFound(query.attemptId)

        check(attempt.memberId == query.memberId) { throw ExamAttemptException.Forbidden() }
        check(attempt.status == ExamAttemptStatus.SUBMITTED) { throw ExamAttemptException.NotSubmitted() }

        val answers = examAttemptReader.findResultAnswers(query.attemptId)

        val answerSheets = questionScoringPort.findAnswerSheets(
            answers.map { QuestionItemId(it.questionItemId) }
        ).associateBy { it.questionItemId }

        val items = answers.map { answer ->
            val answerSheet = answerSheets[QuestionItemId(answer.questionItemId)]
                ?: throw ExamAttemptException.NotFound(answer.questionItemId)

            val correct = answer.selectedNumber == answerSheet.correctNumber

            ExamAttemptResultItemView(
                questionItemId = answer.questionItemId,
                selectedNumber = answer.selectedNumber,
                correctNumber = answerSheet.correctNumber,
                correct = correct,
                timeSpentSeconds = answer.timeSpentSeconds,
            )
        }

        val totalCount = items.size
        val correctCount = items.count { it.correct }
        val totalTimeSpentSeconds = answers.sumOf { it.timeSpentSeconds }

        return ExamAttemptResultView(
            attemptId = attempt.attemptId,
            examId = attempt.examId,
            status = attempt.status,
            totalCount = totalCount,
            correctCount = correctCount,
            score =
                if (totalCount == 0) 0.0
                else correctCount * 100.0 / totalCount,
            accuracy =
                if (totalCount == 0) 0.0
                else correctCount.toDouble() / totalCount,
            timeSpentSeconds = totalTimeSpentSeconds,
            submittedAt = requireNotNull(attempt.submittedAt),
            items = items
        )
    }
}
