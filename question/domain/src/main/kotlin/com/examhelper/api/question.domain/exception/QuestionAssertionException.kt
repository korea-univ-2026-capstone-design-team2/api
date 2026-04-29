package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class QuestionAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    // ── QuestionContent ────────────────────────────────────────
    class StemBlank : QuestionAssertionException(
        "QUESTION_STEM_BLANK",
        "질문 본문이 비어있습니다"
    )

    // ── Passage ────────────────────────────────────────────────
    class PassageContentBlank : QuestionAssertionException(
        "QUESTION_PASSAGE_CONTENT_BLANK",
        "지문 내용이 비어있습니다"
    )

    // ── Exhibit ────────────────────────────────────────────────
    class PropositionSizeMismatch(size: Int) : QuestionAssertionException(
        "QUESTION_PROPOSITION_SIZE_MISMATCH",
        "보기 명제는 2~5개여야 합니다: $size"
    )
    class PropositionLabelDuplicated : QuestionAssertionException(
        "QUESTION_PROPOSITION_LABEL_DUPLICATED",
        "보기 명제 라벨이 중복됩니다"
    )
    class ExhibitContentBlank : QuestionAssertionException(
        "QUESTION_EXHIBIT_CONTENT_BLANK",
        "보기 내용이 비어있습니다"
    )

    // ── AnswerSheet ────────────────────────────────────────────
    class ChoiceSizeMismatch(size: Int) : QuestionAssertionException(
        "QUESTION_CHOICE_SIZE_MISMATCH",
        "선지는 5개여야 합니다: $size"
    )
    class ChoiceNumberDuplicated : QuestionAssertionException(
        "QUESTION_CHOICE_NUMBER_DUPLICATED",
        "선지 번호가 중복됩니다"
    )
    class CorrectAnswerCountMismatch(count: Int) : QuestionAssertionException(
        "QUESTION_CORRECT_ANSWER_COUNT_MISMATCH",
        "정답은 1개여야 합니다: $count"
    )
    class CorrectNumberOutOfRange(number: Int) : QuestionAssertionException(
        "QUESTION_CORRECT_NUMBER_OUT_OF_RANGE",
        "정답 번호는 1~5여야 합니다: $number"
    )
    class CorrectNumberChoiceMismatch(number: Int) : QuestionAssertionException(
        "QUESTION_CORRECT_NUMBER_CHOICE_MISMATCH",
        "correctNumber($number)와 isCorrect 표시가 일치하지 않습니다"
    )
    class ChoiceContentBlank : QuestionAssertionException(
        "QUESTION_CHOICE_CONTENT_BLANK",
        "선지 내용이 비어있습니다"
    )
    class ChoiceNumberOutOfRange(number: Int) : QuestionAssertionException(
        "QUESTION_CHOICE_NUMBER_OUT_OF_RANGE",
        "선지 번호는 1~5여야 합니다: $number"
    )
    class PropositionLabelEmpty : QuestionAssertionException(
        "QUESTION_PROPOSITION_LABEL_EMPTY",
        "명제 조합 선지의 라벨이 비어있습니다"
    )

    // ── Question (교차 검증) ───────────────────────────────────
    class ExhibitAnswerTypeMismatch : QuestionAssertionException(
        "QUESTION_EXHIBIT_ANSWER_TYPE_MISMATCH",
        "명제형 보기에는 명제 조합형 선지가 필요합니다"
    )
    class PassageTopicWithoutPassage : QuestionAssertionException(
        "QUESTION_PASSAGE_TOPIC_WITHOUT_PASSAGE",
        "지문이 없는 문제에는 지문 소재를 설정할 수 없습니다"
    )
    class SubTypeMismatch(questionType: String, subType: String) : QuestionAssertionException(
        "QUESTION_SUBTYPE_MISMATCH",
        "해당 문제 유형($questionType)에는 소분류($subType)를 사용할 수 없습니다"
    )

    // ── QuestionMetadata ───────────────────────────────────────
    class ReadingSubTypeRequired : QuestionAssertionException(
        "QUESTION_READING_SUBTYPE_REQUIRED",
        "독해형 문제는 소분류(QuestionSubType)가 필요합니다"
    )

    // ── QuestionSet ────────────────────────────────────────────
    class QuestionSetSizeMismatch(size: Int) : QuestionAssertionException(
        "QUESTION_SET_SIZE_MISMATCH",
        "세트 문제는 2~5개여야 합니다: $size"
    )

    // ── QualityScore ───────────────────────────────────────────
    class QualityScoreOutOfRange(score: Double) : QuestionAssertionException(
        "QUESTION_QUALITY_SCORE_OUT_OF_RANGE",
        "품질 점수는 0.0~1.0이어야 합니다: $score"
    )

    // ── FrameReference ─────────────────────────────────────────
    class SimilarityScoreOutOfRange(score: Double) : QuestionAssertionException(
        "QUESTION_SIMILARITY_SCORE_OUT_OF_RANGE",
        "유사도 점수는 0.0~1.0이어야 합니다: $score"
    )

    // ── Explanation ────────────────────────────────────────────
    class ExplanationCorrectReasonBlank : QuestionAssertionException(
        "QUESTION_EXPLANATION_CORRECT_REASON_BLANK",
        "정답 해설이 비어있습니다"
    )
}
