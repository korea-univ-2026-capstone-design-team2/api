package com.examhelper.api.exam.adapter.web.dto.response

import com.examhelper.api.exam.port.inbound.view.ExamItemView
import io.swagger.v3.oas.annotations.media.Schema

data class ExamItemResDto(
    @Schema(description = "시험 문항 ID") val examItemId: Long,
    @Schema(description = "문제 ID") val questionId: Long,
    @Schema(description = "문항 순서") val ordering: Int,
) {
    companion object {
        fun fromView(view: ExamItemView): ExamItemResDto = ExamItemResDto(
            examItemId = view.examItemId,
            questionId = view.questionId,
            ordering = view.ordering,
        )
    }
}
