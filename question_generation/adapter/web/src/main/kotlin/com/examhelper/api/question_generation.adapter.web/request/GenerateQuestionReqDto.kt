package com.examhelper.api.question_generation.adapter.web.request

import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory
import com.examhelper.api.question_generation.port.inbound.command.GenerateQuestionCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class GenerateQuestionReqDto(
    @field:NotBlank(message = "subject는 필수입니다")
    @Schema(description = "과목", example = "VERBAL_LOGIC")
    val subject: String,

    @Schema(description = "문제 유형: null이면 random", example = "READING")
    val questionType: String?,      // null이면 random

    @Schema(description = "문제 세부 유형 (선택): null이면 random")
    val questionSubType: String?,   // null이면 random (questionType 범위 내에서)

    @Schema(description = "난이도: null이면 random", example = "MEDIUM")
    val difficulty: String?,

    @Schema(description = "주제 카테고리: null이면 random", example = "HISTORY")
    val topicCategory: String?,

    @Schema(description = "주제 키워드 (선택): null이면 random", example = "")
    val topicKeyword: String?,

    @Schema(description = "주제 설명 (선택): null이면 random", example = "")
    val topicDescription: String?,

    @field:Min(value = 1, message = "quantity는 1 이상이어야 합니다")
    @field:Max(value = 20, message = "quantity는 20 이하여야 합니다")
    @Schema(description = "생성할 문항 수", example = "1")
    val quantity: Int
) {
    fun toCommand(memberId: Long): GenerateQuestionCommand {
        return GenerateQuestionCommand(
            memberId = MemberId(memberId),
            subject = enumValueOrThrow<Subject>(subject),
            questionType = questionType?.let(::enumValueOrThrow),
            questionSubType = questionSubType?.let(::enumValueOrThrow),
            difficulty = difficulty?.let(::enumValueOrThrow),
            topicCategory = topicCategory?.let(::enumValueOrThrow),
            topicKeyword = topicKeyword,
            topicDescription = topicDescription,
            quantity = quantity
        )
    }

    private inline fun <reified T : Enum<T>> enumValueOrThrow(value: String): T =
        enumValues<T>().find { it.name == value }
            ?: throw IllegalArgumentException(
                "유효하지 않은 값: '$value'. 허용값: ${enumValues<T>().joinToString { it.name }}"
            )
}
