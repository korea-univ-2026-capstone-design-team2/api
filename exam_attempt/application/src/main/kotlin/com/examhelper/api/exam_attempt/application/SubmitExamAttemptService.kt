package com.examhelper.api.exam_attempt.application

import com.examhelper.api.exam_attempt.domain.entity.ExamAttemptAnswer
import com.examhelper.api.exam_attempt.domain.exception.ExamAttemptException
import com.examhelper.api.exam_attempt.port.inbound.SubmitExamAttemptUseCase
import com.examhelper.api.exam_attempt.port.inbound.command.SubmitExamAttemptCommand
import com.examhelper.api.exam_attempt.port.inbound.result.SubmitExamAttemptResult
import com.examhelper.api.exam_attempt.port.outbound.ExamAttemptStore
import com.examhelper.api.kernel.identifier.QuestionItemId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class SubmitExamAttemptService(
    private val examAttemptStore: ExamAttemptStore,
) : SubmitExamAttemptUseCase {
    @Transactional
    override fun execute(command: SubmitExamAttemptCommand): SubmitExamAttemptResult {
        val attempt = examAttemptStore.loadById(command.attemptId)
            ?: throw ExamAttemptException.NotFound(command.attemptId.value)

        check(attempt.memberId == command.memberId) { throw ExamAttemptException.Forbidden() }

        command.answers.forEach { answer ->
            attempt.saveAnswer(
                ExamAttemptAnswer(
                    questionItemId = QuestionItemId(answer.questionItemId),
                    selectedNumber = answer.selectedNumber,
                    timeSpentSeconds = answer.timeSpentSeconds,
                    markedUnknown = answer.markedUnknown,
                    bookmarked = answer.bookmarked
                )
            )
        }

        attempt.submit(Instant.now())

        examAttemptStore.save(attempt)

        return SubmitExamAttemptResult(
            attemptId = attempt.id.value,
            examId = attempt.examId.value,
            status = attempt.status.name,
            submittedAt = attempt.submittedAt!!,
        )
    }
}
