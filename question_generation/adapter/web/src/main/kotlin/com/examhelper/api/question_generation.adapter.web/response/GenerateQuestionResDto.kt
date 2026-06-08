package com.examhelper.api.question_generation.adapter.web.response

import com.examhelper.api.question_generation.port.inbound.result.GenerateQuestionResult

data class GenerateQuestionResDto(
    val generationId: String,
    val questionIds: List<String>,
    val successCount: Int,
    val failCount: Int,
    val status: String
) {
    companion object {
        fun fromResult(result: GenerateQuestionResult): GenerateQuestionResDto {
            return GenerateQuestionResDto(
                generationId = result.questionGenerationId.value.toString(),
                questionIds = result.questionIds.map { it.value.toString() },
                successCount = result.successCount,
                failCount = result.failCount,
                status = result.status.name
            )
        }
    }
}
