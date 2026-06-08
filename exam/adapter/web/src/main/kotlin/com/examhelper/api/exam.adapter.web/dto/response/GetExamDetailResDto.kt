package com.examhelper.api.exam.adapter.web.dto.response

import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.port.inbound.view.ExamDetailView
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

data class GetExamDetailResDto(
    @Schema(description = "시험지 ID") val examId: String,
    @Schema(description = "시험지 제목") val title: String,
    @Schema(description = "과목") val subject: Subject,
    @Schema(description = "문제 유형") val questionType: QuestionType,
    @Schema(description = "문제 세부 유형") val questionSubType: QuestionSubType?,
    @Schema(description = "난이도") val difficulty: DifficultyLevel,
    @Schema(description = "주제 카테고리") val topicCategory: TopicCategory,
    @Schema(description = "주제 키워드") val topicKeyword: String?,
    @Schema(description = "주제 설명") val topicDescription: String?,
    @Schema(description = "목표 문항 수") val targetQuestionCount: Int,
    @Schema(description = "시험 상태") val status: ExamStatus,
    @Schema(description = "문항 생성 요청 ID") val generationId: String?,
    @Schema(description = "생성 성공 문항 수") val generationSuccessCount: Int?,
    @Schema(description = "생성 실패 문항 수") val generationFailCount: Int?,
    @Schema(description = "문항 목록") val items: List<ExamItemResDto>,
    @Schema(description = "생성 일시") val createdAt: Instant,
    @Schema(description = "수정 일시") val updatedAt: Instant,
) {
    companion object {
        fun fromView(view: ExamDetailView): GetExamDetailResDto = GetExamDetailResDto(
            examId = view.examId.toString(),
            title = view.title,
            subject = view.subject,
            questionType = view.questionType,
            questionSubType = view.questionSubType,
            difficulty = view.difficulty,
            topicCategory = view.topicCategory,
            topicKeyword = view.topicKeyword,
            topicDescription = view.topicDescription,
            targetQuestionCount = view.targetQuestionCount,
            status = view.status,
            generationId = view.generationId.toString(),
            generationSuccessCount = view.generationSuccessCount,
            generationFailCount = view.generationFailCount,
            items = view.items.map(ExamItemResDto::fromView),
            createdAt = view.createdAt,
            updatedAt = view.updatedAt,
        )
    }
}
