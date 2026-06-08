package com.examhelper.api.exam.adapter.web.dto.response

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.port.inbound.result.GenerateExamResult
import io.swagger.v3.oas.annotations.media.Schema

data class GenerateExamResDto(
    @Schema(description = "생성된 시험지 ID", example = "1234567890")
    val examId: String,

    @Schema(description = "시험지 상태", example = "READY")
    val status: ExamStatus,

    @Schema(description = "문항 생성 요청 ID (생성 호출 실패 시 null)")
    val generationId: String?,

    @Schema(description = "생성 성공 문항 수", example = "5")
    val successCount: Int,

    @Schema(description = "생성 실패 문항 수", example = "0")
    val failCount: Int
) {
    companion object {
        fun fromResult(result: GenerateExamResult): GenerateExamResDto = GenerateExamResDto(
            examId       = result.examId.value.toString(),
            status       = result.status,
            generationId = result.generationId?.value.toString(),
            successCount = result.successCount,
            failCount    = result.failCount,
        )
    }
}
