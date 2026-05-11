package com.examhelper.api.question_generation.adapter.web.response

import com.examhelper.api.question_generation.port.inbound.result.GenerateQuestionResult

data class GenerateQuestionResDto(
    val generationId: Long,
    val questionIds: List<Long>,
    val successCount: Int,
    val failCount: Int,
    val status: String
) {
    companion object {
        fun fromResult(result: GenerateQuestionResult): GenerateQuestionResDto {
            return GenerateQuestionResDto(
                generationId = result.questionGenerationId.value,
                questionIds = result.questionIds.map { it.value },
                successCount = result.successCount,
                failCount = result.failCount,
                status = result.status.name
            )
        }
    }
}
