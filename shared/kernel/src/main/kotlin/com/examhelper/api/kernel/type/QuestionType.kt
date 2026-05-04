package com.examhelper.api.kernel.type

enum class QuestionType(val korean: String) {
    READING("독해") {
        override fun compatibleSubTypes() = listOf(
            QuestionSubType.MATCH,
            QuestionSubType.KNOWABLE,
            QuestionSubType.CONTEXT_CORRECTION,
            QuestionSubType.BLANK_FILLING,
        )
    },
    LOGIC_PUZZLE("논리퀴즈") {
        override fun compatibleSubTypes() = listOf(
            QuestionSubType.MATCH,
            QuestionSubType.KNOWABLE,
        )
    },
    ARGUMENTATION("논증") {
        override fun compatibleSubTypes() = listOf(
            QuestionSubType.CORE_ARGUMENT,
            QuestionSubType.INFERENCE,
            QuestionSubType.ARGUMENT_ANALYSIS,
            QuestionSubType.STRENGTHEN_WEAKEN,
        )
    };

    abstract fun compatibleSubTypes(): List<QuestionSubType>
}
