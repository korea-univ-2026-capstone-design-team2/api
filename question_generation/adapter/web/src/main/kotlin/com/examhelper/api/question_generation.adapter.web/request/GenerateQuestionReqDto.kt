package com.examhelper.api.question_generation.adapter.web.request

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.question_generation.port.inbound.command.GenerateQuestionCommand
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class GenerateQuestionReqDto(
    @field:NotBlank(message = "subject는 필수입니다")
    val subject: String,
    val questionType: String?,      // null이면 random
    val questionSubType: String?,   // null이면 random (questionType 범위 내에서)
    val difficulty: String?,        // null이면 random

    val topicCategory: String?,     // null이면 random 키워드 주입
    val topicKeyword: String?,
    val topicDescription: String?,

    @field:Min(value = 1, message = "quantity는 1 이상이어야 합니다")
    @field:Max(value = 20, message = "quantity는 20 이하여야 합니다")
    val quantity: Int
) {
    fun toCommand(): GenerateQuestionCommand {
        val resolvedSubject = enumValueOrThrow<Subject>(subject)

        val resolvedQuestionType = questionType?.let { enumValueOrThrow<QuestionType>(it) }
            ?: QuestionType.entries.random()

        val resolvedQuestionSubType = questionSubType
            ?.let { enumValueOrThrow<QuestionSubType>(it) }
            ?: resolvedQuestionType.compatibleSubTypes().randomOrNull()

        val resolvedDifficulty = difficulty?.let { enumValueOrThrow<DifficultyLevel>(it) }
            ?: DifficultyLevel.entries.random()

        val resolvedTopicCategory = topicCategory ?: resolvedSubject.defaultTopicCategories().random()


        return GenerateQuestionCommand(
            subject = Subject.valueOf(subject),
            questionType = resolvedQuestionType,
            questionSubType = resolvedQuestionSubType,
            difficulty = resolvedDifficulty,
            topicCategory = resolvedTopicCategory,
            topicKeyword = topicKeyword,
            topicDescription = topicDescription,
            quantity = quantity
        )
    }

    private inline fun <reified T : Enum<T>> enumValueOrThrow(value: String): T =
        enumValues<T>().find { it.name == value }
            ?: throw IllegalArgumentException(
                "유효하지 않은 값: '$value'. 허용값: ${enumValues<T>().map { it.name }}"
            )
}
