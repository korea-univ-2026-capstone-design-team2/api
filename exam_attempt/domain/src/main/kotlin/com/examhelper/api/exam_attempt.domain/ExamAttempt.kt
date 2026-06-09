package com.examhelper.api.exam_attempt.domain

import com.examhelper.api.exam_attempt.domain.entity.ExamAttemptAnswer
import com.examhelper.api.exam_attempt.domain.event.ExamAttemptStartedEvent
import com.examhelper.api.exam_attempt.domain.exception.ExamAttemptAssertionException
import com.examhelper.api.exam_attempt.domain.exception.ExamAttemptException
import com.examhelper.api.exam_attempt.domain.type.ExamAttemptStatus
import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.ExamAttemptId
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.MemberId
import java.time.Instant

class ExamAttempt private constructor(
    id: ExamAttemptId,
    val examId: ExamId,
    val memberId: MemberId,
    answers: List<ExamAttemptAnswer>,
    status: ExamAttemptStatus,
    val startedAt: Instant,
    submittedAt: Instant?,
    val createdAt: Instant,
    updatedAt: Instant
) : AggregateRoot<ExamAttemptId>(id) {
    private val _answers: MutableList<ExamAttemptAnswer> = answers.toMutableList()
    val answers: List<ExamAttemptAnswer> get() = _answers.toList()

    var status: ExamAttemptStatus = status
        private set

    var submittedAt: Instant? = submittedAt
        private set

    var updatedAt: Instant = updatedAt
        private set

    fun saveAnswer(
        answer: ExamAttemptAnswer,
    ) {
        check(status == ExamAttemptStatus.IN_PROGRESS) { throw ExamAttemptAssertionException.CannotModifySubmitted() }

        val existing = _answers.firstOrNull { it.questionItemId == answer.questionItemId }

        if (existing == null) {
            _answers.add(answer)
        } else {
            existing.update(
                selectedNumber = answer.selectedNumber,
                timeSpentSeconds = answer.timeSpentSeconds,
                markedUnknown = answer.markedUnknown,
                bookmarked = answer.bookmarked,
            )
        }

        updatedAt = Instant.now()
    }

    fun submit(submittedAt: Instant) {
        check(status == ExamAttemptStatus.IN_PROGRESS) { throw ExamAttemptAssertionException.AlreadySubmitted() }

        status = ExamAttemptStatus.SUBMITTED
        this.submittedAt = submittedAt
        updatedAt = submittedAt
    }

    fun validateOwner(memberId: MemberId) {
        if (this.memberId != memberId) throw ExamAttemptException.Forbidden()
    }

    private fun validate() {
        require(_answers.map { it.questionItemId }.distinct().size == _answers.size) {
            throw ExamAttemptAssertionException.DuplicateQuestionItemIds()
        }
    }

    companion object {
        fun start(
            id: ExamAttemptId,
            examId: ExamId,
            memberId: MemberId,
        ): ExamAttempt {
            val now = Instant.now()

            return ExamAttempt(
                id = id,
                examId = examId,
                memberId = memberId,
                answers = emptyList(),
                status = ExamAttemptStatus.IN_PROGRESS,
                startedAt = now,
                submittedAt = null,
                createdAt = now,
                updatedAt = now,
            ).also {
                it.validate()
                it.addDomainEvent(
                    ExamAttemptStartedEvent(
                        attemptId = id.value,
                        examId = examId.value,
                        memberId = memberId.value,
                        occurredAt = now,
                    )
                )
            }
        }

        fun of(
            id: ExamAttemptId,
            examId: ExamId,
            memberId: MemberId,
            answers: List<ExamAttemptAnswer>,
            status: ExamAttemptStatus,
            startedAt: Instant,
            submittedAt: Instant?,
            createdAt: Instant,
            updatedAt: Instant,
        ): ExamAttempt =
            ExamAttempt(
                id = id,
                examId = examId,
                memberId = memberId,
                answers = answers,
                status = status,
                startedAt = startedAt,
                submittedAt = submittedAt,
                createdAt = createdAt,
                updatedAt = updatedAt,
            ).also { it.validate() }
    }
}
