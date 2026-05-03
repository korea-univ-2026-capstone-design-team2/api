package com.examhelper.api.question_generation.adapter.domain_connector

import com.examhelper.api.kernel.identifier.LogicalFrameId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.vo.AnswerChoice
import com.examhelper.api.question.domain.vo.AnswerSheet
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Explanation
import com.examhelper.api.question.domain.vo.FrameReference
import com.examhelper.api.question.domain.vo.Passage
import com.examhelper.api.question.domain.vo.PassageTopic
import com.examhelper.api.question.domain.vo.Proposition
import com.examhelper.api.question.port.inbound.CreateQuestionUseCase
import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand
import com.examhelper.api.question_generation.port.outbound.QuestionCreationPort
import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationCommand
import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationMetadata
import com.examhelper.api.question_generation.port.outbound.result.LlmChoiceResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExhibitResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExplanationResult
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult
import com.examhelper.api.question_generation.port.outbound.result.LlmPassageResult
import com.examhelper.api.question_generation.port.outbound.result.QuestionCreationResult
import org.springframework.stereotype.Component

@Component
class QuestionCreationAdapter(
    private val createQuestionUseCase: CreateQuestionUseCase
) : QuestionCreationPort {

    override fun create(command: QuestionCreationCommand): QuestionCreationResult {
        val result = createQuestionUseCase.execute(command.toCreateQuestionCommand())
        return QuestionCreationResult(QuestionId(result.questionId))
    }

    private fun QuestionCreationCommand.toCreateQuestionCommand(): CreateQuestionCommand =
        CreateQuestionCommand(
            generationId = generationId.value,
            stem = result.stem,
            passage = result.passage?.toDomain(),
            exhibit = result.exhibit?.toDomain(),
            answerSheet = result.toAnswerSheet(),
            subject = metadata.subject,
            questionType = metadata.questionType,
            questionSubType = metadata.questionSubType,
            difficulty = metadata.difficulty,
            passageTopic = toPassageTopic(),
            explanation = result.explanation.toDomain(),
            sourceFrame = metadata.toFrameReference(),
        )

    // ── LlmPassageResult → Passage ────────────────────────────
    private fun LlmPassageResult.toDomain(): Passage =
        Passage.TextPassage(
            content = content,
            description = description,
        )

    // ── LlmExhibitResult → Exhibit ────────────────────────────
    private fun LlmExhibitResult.toDomain(): Exhibit =
        when (this) {
            is LlmExhibitResult.Proposition ->
                Exhibit.PropositionExhibit(
                    propositions = propositions.map { p ->
                        Proposition(
                            label = p.label,
                            content = p.content,
                        )
                    }
                )

            is LlmExhibitResult.Text ->
                Exhibit.TextExhibit(content = content)
        }

    // ── LlmGenerationResult → AnswerSheet ─────────────────────
    private fun LlmGenerationResult.toAnswerSheet(): AnswerSheet {
        val answerChoices = choices.map { it.toDomain() }
        val correctNumber = choices.first { it.isCorrect }.number
        return AnswerSheet.MultipleChoiceSheet(
            choices = answerChoices,
            correctNumber = correctNumber,
        )
    }

    private fun LlmChoiceResult.toDomain(): AnswerChoice =
        when (this) {
            is LlmChoiceResult.Text ->
                AnswerChoice.TextChoice(
                    number = number,
                    content = content,
                    isCorrect = isCorrect,
                )

            is LlmChoiceResult.PropositionCombination ->
                AnswerChoice.PropositionCombinationChoice(
                    number = number,
                    labels = labels,
                    isCorrect = isCorrect,
                )
        }

    // ── LlmExplanationResult → Explanation ────────────────────
    private fun LlmExplanationResult.toDomain(): Explanation =
        Explanation(
            correctReason = correctReason,
            incorrectReasons = incorrectReasons,
        )

    // ── QuestionCreationMetadata → PassageTopic ───────────────
    private fun QuestionCreationCommand.toPassageTopic(): PassageTopic? =
        result.passage?.let {
            PassageTopic(
                category = metadata.topicCategory,
                keyword = metadata.topicKeyword
            )
        }

    // ── QuestionCreationMetadata → FrameReference ─────────────
    private fun QuestionCreationMetadata.toFrameReference(): FrameReference =
        FrameReference(
            frameId = LogicalFrameId(1L),
            similarityScore = similarityScore,
            frameType = questionType.name
        )
}
