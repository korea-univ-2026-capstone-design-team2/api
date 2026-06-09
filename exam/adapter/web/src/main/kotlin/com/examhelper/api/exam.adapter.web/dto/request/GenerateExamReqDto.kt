package com.examhelper.api.exam.adapter.web.dto.request

import com.examhelper.api.exam.port.inbound.command.GenerateExamCommand
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory
import io.swagger.v3.oas.annotations.media.Schema

data class GenerateExamReqDto(
    @Schema(description = "시험지 제목", example = "2025 언어논리 실전 모의고사")
    val title: String,

    @Schema(description = "과목", example = "VERBAL_LOGIC")
    val subject: Subject,

    @Schema(description = "문제 유형", example = "READING")
    val questionType: QuestionType,

    @Schema(description = "문제 세부 유형 (선택)")
    val questionSubType: QuestionSubType?,

    @Schema(description = "난이도", example = "MEDIUM")
    val difficulty: DifficultyLevel,

    @Schema(description = "주제 카테고리", example = "POLITICS")
    val topicCategory: TopicCategory,

    @Schema(description = "주제 키워드 (선택)", example = "선거제도")
    val topicKeyword: String?,

    @Schema(description = "주제 설명 (선택)", example = "비례대표제와 다수대표제의 장단점 비교")
    val topicDescription: String?,

    @Schema(description = "생성할 문항 수", example = "5")
    val targetQuestionCount: Int,

    @Schema(description = "RAG 프레임 검색 수", example = "3", defaultValue = "3")
    val frameSearchTopK: Int = 3,
) {
    fun toCommand(): GenerateExamCommand =
        GenerateExamCommand(
            title = title,
            subject = subject,
            questionType = questionType,
            questionSubType = questionSubType,
            difficulty = difficulty,
            topicCategory = topicCategory,
            topicKeyword = topicKeyword,
            topicDescription = topicDescription,
            targetQuestionCount = targetQuestionCount,
            frameSearchTopK = frameSearchTopK,
        )
}
