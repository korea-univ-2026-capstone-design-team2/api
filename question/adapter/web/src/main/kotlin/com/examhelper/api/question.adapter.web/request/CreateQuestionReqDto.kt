package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory
import com.examhelper.api.question.domain.vo.PassageTopic
import com.examhelper.api.question.domain.vo.QuestionMetadata
import com.examhelper.api.question.domain.vo.SharedQuestionContext
import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand

data class CreateQuestionReqDto(
    val generationId: Long,
    val sharedContext: SharedContextReqDto?,
    val metadata: QuestionGroupMetadataReqDto,
    val questions: List<QuestionReqDto>,
) {
    fun toCommand(): CreateQuestionCommand =
        CreateQuestionCommand(
            generationId = generationId,
            sharedContext = sharedContext?.toDomain(),
            metadata = metadata.toDomain(),
            questions = questions.map { it.toCommand() },
        )

    data class SharedContextReqDto(
        val content: String,
        val description: String?,
    ) {
        fun toDomain(): SharedQuestionContext =
            SharedQuestionContext.Text(
                content = content,
                description = description,
            )
    }

    data class QuestionGroupMetadataReqDto(
        val subject: Subject,
        val questionType: QuestionType,
        val questionSubType: QuestionSubType?,
        val difficulty: DifficultyLevel,
        val passageTopicCategory: String?,
        val passageTopicKeyword: String?,
    ) {
        fun toDomain(): QuestionMetadata =
            QuestionMetadata(
                subject = subject,
                questionType = questionType,
                questionSubType = questionSubType,
                difficulty = difficulty,
                passageTopic = passageTopicCategory?.let {
                    PassageTopic(category = TopicCategory.fromString(it), keyword = passageTopicKeyword)
                },
            )
    }

    data class QuestionReqDto(
        val stem: String,
        val exhibit: ExhibitReqDto?,
        val answerSheet: AnswerSheetReqDto,
        val explanation: ExplanationReqDto,
        val questionSubType: QuestionSubType?,
    ) {
        fun toCommand(): CreateQuestionCommand.QuestionCommand =
            CreateQuestionCommand.QuestionCommand(
                stem = stem,
                exhibit = exhibit?.toDomain(),
                answerSheet = answerSheet.toDomain(),
                explanation = explanation.toDomain()
            )
    }
}
