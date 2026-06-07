package com.examhelper.api.learning_statistics.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class DailyLearningStatAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class InvalidMemberId(
        memberId: Long,
    ) : DailyLearningStatAssertionException(
        "DAILY_LEARNING_STAT_INVALID_MEMBER_ID",
        "유효하지 않은 회원 ID 입니다. memberId=$memberId"
    )

    class NegativeQuestionCount(
        questionCount: Int,
    ) : DailyLearningStatAssertionException(
        "DAILY_LEARNING_STAT_NEGATIVE_QUESTION_COUNT",
        "문제 수는 음수일 수 없습니다. questionCount=$questionCount"
    )

    class NegativeCorrectCount(
        correctCount: Int,
    ) : DailyLearningStatAssertionException(
        "DAILY_LEARNING_STAT_NEGATIVE_CORRECT_COUNT",
        "정답 수는 음수일 수 없습니다. correctCount=$correctCount"
    )

    class CorrectCountExceedsQuestionCount(
        correctCount: Int,
        questionCount: Int,
    ) : DailyLearningStatAssertionException(
        "DAILY_LEARNING_STAT_CORRECT_COUNT_EXCEEDS_QUESTION_COUNT",
        "정답 수가 총 문제 수를 초과할 수 없습니다. correctCount=$correctCount, questionCount=$questionCount"
    )

    class NegativeStudySeconds(
        studySeconds: Long,
    ) : DailyLearningStatAssertionException(
        "DAILY_LEARNING_STAT_NEGATIVE_STUDY_SECONDS",
        "학습 시간은 음수일 수 없습니다. studySeconds=$studySeconds"
    )

    class NegativeAdditionalStudySeconds(
        studySeconds: Int,
    ) : DailyLearningStatAssertionException(
        "DAILY_LEARNING_STAT_NEGATIVE_ADDITIONAL_STUDY_SECONDS",
        "추가되는 학습 시간은 음수일 수 없습니다. studySeconds=$studySeconds"
    )
}
