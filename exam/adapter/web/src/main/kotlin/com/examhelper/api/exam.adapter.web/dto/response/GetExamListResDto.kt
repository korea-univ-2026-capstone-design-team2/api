package com.examhelper.api.exam.adapter.web.dto.response

import com.examhelper.api.exam.port.inbound.query.GetExamListResult
import io.swagger.v3.oas.annotations.media.Schema

data class GetExamListResDto(
    @Schema(description = "시험 목록")
    val items: List<ExamSummaryResDto>,

    @Schema(description = "전체 개수")
    val totalCount: Long,

    @Schema(description = "현재 페이지")
    val page: Int,

    @Schema(description = "페이지 크기")
    val size: Int
) {
    companion object {
        fun fromResult(result: GetExamListResult): GetExamListResDto =
            GetExamListResDto(
                items = result.items.map(ExamSummaryResDto::fromView),
                totalCount = result.totalCount,
                page = result.page,
                size = result.size,
            )
    }
}
