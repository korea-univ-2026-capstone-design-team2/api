package com.examhelper.api.learning_statistics.domain

import com.examhelper.api.learning_statistics.domain.exception.DailyLearningStatAssertionException
import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.identifier.DailyLearningStatId
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.Subject
import java.time.Instant
import java.time.LocalDate

class DailyLearningStat private constructor(
    id: DailyLearningStatId,
    val memberId: MemberId,
    val date: LocalDate,
    val subject: Subject,
    questionCount: Int,
    correctCount: Int,
    studySeconds: Long,
    val createdAt: Instant,
    var updatedAt: Instant,
) : AggregateRoot<DailyLearningStatId>(id) {
    var questionCount: Int = questionCount
        private set

    var correctCount: Int = correctCount
        private set

    var studySeconds: Long = studySeconds
        private set

    // 학습 결과 누적 ──────────────────────────────
    fun accumulate(isCorrect: Boolean, timeSpentSeconds: Int) {
        require(timeSpentSeconds >= 0) { "추가되는 풀이 시간은 음수일 수 없습니다: $timeSpentSeconds" }

        this.questionCount += 1
        if (isCorrect) { this.correctCount += 1 }
        this.studySeconds += timeSpentSeconds
        this.updatedAt = Instant.now()

        validate()
    }

    private fun validate() {
        require(memberId.value > 0) { throw DailyLearningStatAssertionException.InvalidMemberId(memberId.value) }

        require(questionCount >= 0) { throw DailyLearningStatAssertionException.NegativeQuestionCount(questionCount) }

        require(correctCount >= 0) { throw DailyLearningStatAssertionException.NegativeCorrectCount(correctCount) }
        check(questionCount >= correctCount) {
            throw DailyLearningStatAssertionException.CorrectCountExceedsQuestionCount(
                correctCount = correctCount,
                questionCount = questionCount,
            )
        }

        require(studySeconds >= 0) { throw DailyLearningStatAssertionException.NegativeStudySeconds(studySeconds) }
    }

    companion object {
        fun create(
            id: DailyLearningStatId,
            memberId: MemberId,
            date: LocalDate,
            subject: Subject,
        ): DailyLearningStat {
            val now = Instant.now()
            return DailyLearningStat(
                id = id,
                memberId = memberId,
                date = date,
                subject = subject,
                questionCount = 0,
                correctCount = 0,
                studySeconds = 0L,
                createdAt = now,
                updatedAt = now
            ).also { it.validate() }
        }

        fun of(
            id: DailyLearningStatId,
            memberId: MemberId,
            date: LocalDate,
            subject: Subject,
            questionCount: Int,
            correctCount: Int,
            studySeconds: Long,
            createdAt: Instant,
            updatedAt: Instant
        ): DailyLearningStat = DailyLearningStat(
            id = id,
            memberId = memberId,
            date = date,
            subject = subject,
            questionCount = questionCount,
            correctCount = correctCount,
            studySeconds = studySeconds,
            createdAt = createdAt,
            updatedAt = updatedAt
        ).also { it.validate() }
    }
}
