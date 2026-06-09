package com.examhelper.api.question.domain.exception

import com.examhelper.api.kernel.core.exception.DomainAssertionException

sealed class QuestionItemAssertionException(
    code: String,
    message: String,
) : DomainAssertionException(code, message) {
    class AlreadyPublished(id: String) : QuestionItemAssertionException(
        code = "QUESTION_ITEM_ALREADY_PUBLISHED",
        message = "이미 출제된 문제입니다: $id",
    )

    class StatusTransitionNotAllowed(from: String, to: String) : QuestionItemAssertionException(
        code = "QUESTION_ITEM_STATUS_TRANSITION_NOT_ALLOWED",
        message = "상태 전이가 허용되지 않습니다: $from -> $to",
    )

    class QualityScoreNotAssigned : QuestionItemAssertionException(
        code = "QUESTION_ITEM_QUALITY_SCORE_NOT_ASSIGNED",
        message = "품질 점수가 부여되지 않았습니다",
    )

    class QuestionItemSetAlreadyAssigned(questionSetId: Long) : QuestionItemAssertionException(
        code = "QUESTION_ITEM_SET_ALREADY_ASSIGNED",
        message = "이미 세트에 편입된 문제입니다: set=$questionSetId",
    )

    class AnswerChoiceBlank : QuestionItemAssertionException(
        code = "QUESTION_ITEM_ANSWER_CHOICE_BLANK",
        message = "선지는 비어 있을 수 없습니다.",
    )

    // ── QuestionContent ────────────────────────────────────────
    class StemBlank : QuestionItemAssertionException(
        code = "QUESTION_STEM_BLANK",
        message = "질문 본문이 비어있습니다",
    )

    // ── Passage ────────────────────────────────────────────────
    class PassageContentBlank : QuestionItemAssertionException(
        code = "QUESTION_PASSAGE_CONTENT_BLANK",
        message = "지문 내용이 비어있습니다",
    )

    // ── Exhibit ────────────────────────────────────────────────
    class PropositionSizeMismatch(size: Int) : QuestionItemAssertionException(
        code = "QUESTION_PROPOSITION_SIZE_MISMATCH",
        message = "보기 명제는 2~5개여야 합니다: $size",
    )

    class PropositionLabelDuplicated : QuestionItemAssertionException(
        code = "QUESTION_PROPOSITION_LABEL_DUPLICATED",
        message = "보기 명제 라벨이 중복됩니다",
    )

    class ExhibitContentBlank : QuestionItemAssertionException(
        code = "QUESTION_EXHIBIT_CONTENT_BLANK",
        message = "보기 내용이 비어있습니다",
    )

    // ── AnswerSheet ────────────────────────────────────────────
    class ChoiceSizeMismatch(size: Int) : QuestionItemAssertionException(
        code = "QUESTION_CHOICE_SIZE_MISMATCH",
        message = "선지는 5개여야 합니다: $size",
    )

    class ChoiceNumberDuplicated : QuestionItemAssertionException(
        code = "QUESTION_CHOICE_NUMBER_DUPLICATED",
        message = "선지 번호가 중복됩니다",
    )

    class CorrectAnswerCountMismatch(count: Int) : QuestionItemAssertionException(
        code = "QUESTION_CORRECT_ANSWER_COUNT_MISMATCH",
        message = "정답은 1개여야 합니다: $count",
    )

    class CorrectNumberOutOfRange(number: Int) : QuestionItemAssertionException(
        code = "QUESTION_CORRECT_NUMBER_OUT_OF_RANGE",
        message = "정답 번호는 1~5여야 합니다: $number",
    )

    class CorrectNumberChoiceMismatch(number: Int) : QuestionItemAssertionException(
        code = "QUESTION_CORRECT_NUMBER_CHOICE_MISMATCH",
        message = "correctNumber($number)와 isCorrect 표시가 일치하지 않습니다",
    )

    class ChoiceContentBlank : QuestionItemAssertionException(
        code = "QUESTION_CHOICE_CONTENT_BLANK",
        message = "선지 내용이 비어있습니다",
    )

    class ChoiceNumberOutOfRange(number: Int) : QuestionItemAssertionException(
        code = "QUESTION_CHOICE_NUMBER_OUT_OF_RANGE",
        message = "선지 번호는 1~5여야 합니다: $number",
    )

    class PropositionLabelEmpty : QuestionItemAssertionException(
        code = "QUESTION_PROPOSITION_LABEL_EMPTY",
        message = "명제 조합 선지의 라벨이 비어있습니다",
    )

    // ── Question (교차 검증) ───────────────────────────────────
    class ExhibitAnswerTypeMismatch : QuestionItemAssertionException(
        code = "QUESTION_EXHIBIT_ANSWER_TYPE_MISMATCH",
        message = "명제형 보기에는 명제 조합형 선지가 필요합니다",
    )

    class PassageTopicWithoutPassage : QuestionItemAssertionException(
        code = "QUESTION_PASSAGE_TOPIC_WITHOUT_PASSAGE",
        message = "지문이 없는 문제에는 지문 소재를 설정할 수 없습니다",
    )

    class SubTypeMismatch(questionType: String, subType: String) : QuestionItemAssertionException(
        code = "QUESTION_SUBTYPE_MISMATCH",
        message = "해당 문제 유형($questionType)에는 소분류($subType)를 사용할 수 없습니다",
    )

    // ── QuestionMetadata ───────────────────────────────────────
    class ReadingSubTypeRequired : QuestionItemAssertionException(
        code = "QUESTION_READING_SUBTYPE_REQUIRED",
        message = "독해형 문제는 소분류(QuestionSubType)가 필요합니다",
    )

    // ── QuestionSet ────────────────────────────────────────────
    class QuestionItemSetSizeMismatch(size: Int) : QuestionItemAssertionException(
        code = "QUESTION_SET_SIZE_MISMATCH",
        message = "세트 문제는 2~5개여야 합니다: $size",
    )

    // ── QualityScore ───────────────────────────────────────────
    class QualityScoreOutOfRange(score: Double) : QuestionItemAssertionException(
        code = "QUESTION_QUALITY_SCORE_OUT_OF_RANGE",
        message = "품질 점수는 0.0~1.0이어야 합니다: $score",
    )

    // ── FrameReference ─────────────────────────────────────────
    class SimilarityScoreOutOfRange(score: Double) : QuestionItemAssertionException(
        code = "QUESTION_SIMILARITY_SCORE_OUT_OF_RANGE",
        message = "유사도 점수는 0.0~1.0이어야 합니다: $score",
    )

    // ── Explanation ────────────────────────────────────────────
    class ExplanationCorrectReasonBlank : QuestionItemAssertionException(
        code = "QUESTION_EXPLANATION_CORRECT_REASON_BLANK",
        message = "정답 해설이 비어있습니다",
    )
}
