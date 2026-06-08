package com.examhelper.api.exam.adapter.web.dto.response

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.port.inbound.view.ExamSummaryView
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

data class ExamSummaryResDto(
    @Schema(description = "시험지 ID")      val examId: String,
    @Schema(description = "시험지 제목")     val title: String,
    @Schema(description = "과목")           val subject: Subject,
    @Schema(description = "문제 유형")       val questionType: QuestionType,
    @Schema(description = "난이도")          val difficulty: DifficultyLevel,
    @Schema(description = "목표 문항 수")    val targetQuestionCount: Int,
    @Schema(description = "실제 문항 수")    val actualQuestionCount: Int,
    @Schema(description = "시험 상태")       val status: ExamStatus,
    @Schema(description = "생성 일시")       val createdAt: Instant,
) {
    companion object {
        fun fromView(view: ExamSummaryView): ExamSummaryResDto = ExamSummaryResDto(
            examId              = view.examId.toString(),
            title               = view.title,
            subject             = view.subject,
            questionType        = view.questionType,
            difficulty          = view.difficulty,
            targetQuestionCount = view.targetQuestionCount,
            actualQuestionCount = view.actualQuestionCount,
            status              = view.status,
            createdAt           = view.createdAt,
        )
    }
}
