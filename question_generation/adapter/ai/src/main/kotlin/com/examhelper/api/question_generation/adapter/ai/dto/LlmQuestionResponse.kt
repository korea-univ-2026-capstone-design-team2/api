package com.examhelper.api.question_generation.adapter.ai.dto

data class LlmQuestionResponse(
    val stem        : String,
    val passage     : LlmPassageResponse?,
    val exhibit     : LlmExhibitResponse?,
    val choices     : List<LlmChoiceResponse>,
    val explanation : LlmExplanationResponse,
) {
    data class LlmPassageResponse(
        val content     : String?,
        val description : String?,
    )

    data class LlmExhibitResponse(
        val type         : String?,
        val content      : String?,
        val propositions : List<LlmPropositionResponse>?,
    )

    data class LlmPropositionResponse(
        val label   : String,
        val content : String,
    )

    data class LlmChoiceResponse(
        val number    : Int,
        val isCorrect : Boolean,
        val type      : String,
        val content   : String?,
        val labels    : List<String>?,
    )

    data class LlmExplanationResponse(
        val correctReason    : String,
        val incorrectReasons : Map<String, String>,
    )
}
