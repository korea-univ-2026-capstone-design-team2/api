package com.examhelper.api.question_generation.port.outbound.result

sealed class LlmExhibitResult {
    data class Proposition(
        val propositions: List<LlmPropositionResult>,
    ) : LlmExhibitResult()

    data class Text(
        val content: String,
    ) : LlmExhibitResult()
}
