package com.examhelper.api.exam_attempt.application

import com.examhelper.api.exam_attempt.domain.entity.ExamAttemptAnswer
import com.examhelper.api.exam_attempt.domain.exception.ExamAttemptException
import com.examhelper.api.exam_attempt.port.inbound.SubmitExamAttemptUseCase
import com.examhelper.api.exam_attempt.port.inbound.command.SubmitExamAttemptCommand
import com.examhelper.api.exam_attempt.port.inbound.result.SubmitExamAttemptResult
import com.examhelper.api.exam_attempt.port.outbound.ExamAttemptStore
import com.examhelper.api.exam_attempt.port.outbound.QuestionScoringPort
import com.examhelper.api.exam_attempt.port.outbound.QuestionSummaryPort
import com.examhelper.api.kernel.core.DomainEventPublisher
import com.examhelper.api.kernel.event.ExamAttemptSubmittedEvent
import com.examhelper.api.kernel.event.ExamAttemptSubmittedItem
import com.examhelper.api.kernel.identifier.QuestionItemId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class SubmitExamAttemptService(
    private val examAttemptStore: ExamAttemptStore,
    private val questionScoringPort: QuestionScoringPort,
    private val questionSummaryPort: QuestionSummaryPort,
    private val domainEventPublisher: DomainEventPublisher
) : SubmitExamAttemptUseCase {
    private val now = Instant.now()

    @Transactional
    override fun execute(command: SubmitExamAttemptCommand): SubmitExamAttemptResult {
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
                    bookmarked = answer.bookmarked
                )
            )
        }

        attempt.submit(now)

        examAttemptStore.save(attempt)

        val questionItemIds = attempt.answers.map { it.questionItemId }
        val answerSheets = questionScoringPort.findAnswerSheets(questionItemIds).associateBy { it.questionItemId }
        val questionItems = questionSummaryPort.findSummaries(questionItemIds).associateBy { QuestionItemId(it.questionItemId) }
        val eventItems = attempt.answers.map { answer ->
            val answerSheet = answerSheets[answer.questionItemId]
                ?: throw IllegalStateException("정답 정보를 찾을 수 없습니다. questionItemId=${answer.questionItemId.value}")

            val metadata = questionItems[answer.questionItemId]
                ?: throw IllegalStateException("문제 메타데이터를 찾을 수 없습니다. questionItemId=${answer.questionItemId.value}")

            ExamAttemptSubmittedItem(
                questionItemId = answer.questionItemId.value,
                subject = metadata.subject.name,
                isCorrect = answer.selectedNumber == answerSheet.correctNumber,
                timeSpentSeconds = answer.timeSpentSeconds,
            )
        }

        domainEventPublisher.publish(
            ExamAttemptSubmittedEvent(
                attemptId = attempt.id.value,
                memberId = attempt.memberId.value,
                examId = attempt.examId.value,
                submittedAt = now,
                items = eventItems,
            )
        )


        return SubmitExamAttemptResult(
            attemptId = attempt.id.value,
            examId = attempt.examId.value,
            status = attempt.status.name,
            submittedAt = attempt.submittedAt!!,
        )
    }
}
