package com.examhelper.api.question_generation.adapter.ai.dto

data class LlmGenerationResponse(
    val sharedContext: LlmSharedContextResponse?,
    val questions: List<LlmQuestionResponse>,
) {
    data class LlmSharedContextResponse(
        val content: String?,
        val description: String?,
    )

    data class LlmQuestionResponse(
        val stem: String,
        val exhibit: LlmExhibitResponse?,
        val choices: List<LlmChoiceResponse>,
        val explanation: LlmExplanationResponse,
    )

    data class LlmExhibitResponse(
        val type: String?,
        val propositions: List<LlmPropositionResponse>?,
        val content: String?,
    )

    data class LlmPropositionResponse(
        val label: String,
        val content: String,
    )

    data class LlmChoiceResponse(
        val number: Int,
        val type: String,
        val isCorrect: Boolean,
        val content: String?,
        val labels: List<String>?,
    )

    data class LlmExplanationResponse(
        val correctReason: String,
        val incorrectReasons: Map<String, String>,
    )
}
