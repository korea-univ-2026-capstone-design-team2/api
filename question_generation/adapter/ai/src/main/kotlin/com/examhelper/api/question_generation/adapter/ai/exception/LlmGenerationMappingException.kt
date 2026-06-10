package com.examhelper.api.question_generation.adapter.ai.exception

sealed class LlmGenerationMappingException(message: String) : RuntimeException(message) {
    // Exhibit
    class UnknownExhibitType(type: String?) : LlmGenerationMappingException("알 수 없는 exhibit type: $type")
    class MissingPropositions : LlmGenerationMappingException("PROPOSITION exhibit에 propositions가 없습니다")
    class MissingTextContent : LlmGenerationMappingException("TEXT exhibit에 content가 없습니다")

    // Choice
    class UnknownChoiceType(type: String) : LlmGenerationMappingException("알 수 없는 choice type: $type")
    class MissingChoiceContent(number: Int) : LlmGenerationMappingException("TEXT 선지에 content가 없습니다: $number")
    class MissingChoiceLabels(number: Int) :
        LlmGenerationMappingException("PROPOSITION_COMBINATION 선지에 labels가 없습니다: $number")

    class NoCorrectChoice : LlmGenerationMappingException("정답(isCorrect=true)이 없습니다")
    class MultipleCorrectChoices(count: Int) : LlmGenerationMappingException("정답이 ${count}개입니다. 반드시 1개여야 합니다")

    // PropositionLabel
    class InvalidPropositionLabel(label: String) : LlmGenerationMappingException("유효하지 않은 PropositionLabel: '$label'")
    class InvalidPropositionLabelInChoice(label: String, number: Int) :
        LlmGenerationMappingException("유효하지 않은 PropositionLabel: '$label' (선지 $number)")

    // Explanation
    class InvalidExplanationKey(key: String) : LlmGenerationMappingException("incorrectReasons key가 숫자가 아닙니다: $key")
    class MissingExplanationKeys(keys: Set<Int>) :
        LlmGenerationMappingException("incorrectReasons에 필수 키가 누락되었습니다: $keys")

    // Question
    class EmptyQuestions : LlmGenerationMappingException("생성된 문제가 없습니다")
}
