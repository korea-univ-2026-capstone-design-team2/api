package com.examhelper.api.question_generation.port.outbound.result

import com.examhelper.api.kernel.type.PropositionLabel

sealed class LlmChoiceResult {
    abstract val number   : Int
    abstract val isCorrect: Boolean

    data class Text(
        override val number   : Int,
        override val isCorrect: Boolean,
        val content           : String,
    ) : LlmChoiceResult()

    data class PropositionCombination(
        override val number   : Int,
        override val isCorrect: Boolean,
        val labels            : List<PropositionLabel>,
    ) : LlmChoiceResult()
}
