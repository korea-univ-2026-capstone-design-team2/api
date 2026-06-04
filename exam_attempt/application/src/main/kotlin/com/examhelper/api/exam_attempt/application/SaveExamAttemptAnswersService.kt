package com.examhelper.api.exam_attempt.application

import com.examhelper.api.exam_attempt.domain.entity.ExamAttemptAnswer
import com.examhelper.api.exam_attempt.domain.exception.ExamAttemptException
import com.examhelper.api.exam_attempt.port.inbound.SaveExamAttemptAnswersUseCase
import com.examhelper.api.exam_attempt.port.inbound.command.SaveExamAttemptAnswersCommand
import com.examhelper.api.exam_attempt.port.inbound.result.SaveExamAttemptAnswersResult
import com.examhelper.api.exam_attempt.port.outbound.ExamAttemptStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SaveExamAttemptAnswersService(
    private val examAttemptStore: ExamAttemptStore
) : SaveExamAttemptAnswersUseCase {
    @Transactional
    override fun execute(command: SaveExamAttemptAnswersCommand): SaveExamAttemptAnswersResult {
        val attempt = examAttemptStore.loadById(command.attemptId)
            ?: throw ExamAttemptException.NotFound(command.attemptId.value)

        check(attempt.memberId == command.memberId) { throw ExamAttemptException.Forbidden() }

        command.answers.forEach { answer ->
            attempt.saveAnswer(
                ExamAttemptAnswer(
                    questionItemId = answer.questionItemId,
                    selectedNumber = answer.selectedNumber,
                    timeSpentSeconds = answer.timeSpentSeconds,
                    markedUnknown = answer.markedUnknown,
                    bookmarked = answer.bookmarked,
                )
            )
        }

        examAttemptStore.save(attempt)

        return SaveExamAttemptAnswersResult(
            attemptId = attempt.id.value,
            savedCount = command.answers.size,
            updatedAt = attempt.updatedAt,
        )
    }
}
