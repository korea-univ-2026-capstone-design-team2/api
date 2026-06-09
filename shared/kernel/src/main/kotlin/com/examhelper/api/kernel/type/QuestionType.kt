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
        override fun compatibleSubTypes() = emptyList<QuestionSubType>()
    },
    ARGUMENTATION("논증") {
        override fun compatibleSubTypes() = emptyList<QuestionSubType>()
    };

    abstract fun compatibleSubTypes(): List<QuestionSubType>

    fun resolveSubType(requested: QuestionSubType?): QuestionSubType? =
        when {
            compatibleSubTypes().isEmpty() -> null
            requested != null -> {
                require(requested in compatibleSubTypes()) {
                    "SubType ${requested.name} is not compatible with ${this.name}"
                }
                requested
            }
            else -> QuestionSubType.MATCH
        }
}
