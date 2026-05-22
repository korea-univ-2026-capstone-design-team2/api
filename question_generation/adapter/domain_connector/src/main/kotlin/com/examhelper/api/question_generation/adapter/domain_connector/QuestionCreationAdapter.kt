package com.examhelper.api.question_generation.adapter.domain_connector

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.question.domain.vo.AnswerChoice
import com.examhelper.api.question.domain.vo.AnswerSheet
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Explanation
import com.examhelper.api.question.domain.vo.PassageTopic
import com.examhelper.api.question.domain.vo.Proposition
import com.examhelper.api.question.domain.vo.QuestionMetadata
import com.examhelper.api.question.domain.vo.SharedQuestionContext
import com.examhelper.api.question.port.inbound.CreateQuestionUseCase
import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand
import com.examhelper.api.question_generation.port.outbound.QuestionCreationPort
import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationCommand
import com.examhelper.api.question_generation.port.outbound.result.LlmChoiceResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExhibitResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExplanationResult
import com.examhelper.api.question_generation.port.outbound.result.LlmQuestionResult
import com.examhelper.api.question_generation.port.outbound.result.LlmSharedContextResult
import com.examhelper.api.question_generation.port.outbound.result.QuestionCreationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class QuestionCreationAdapter(
    private val createQuestionGroupUseCase: CreateQuestionUseCase
) : QuestionCreationPort {

    override suspend fun create(command: QuestionCreationCommand): QuestionCreationResult =
        withContext(Dispatchers.IO) {
            val result = createQuestionGroupUseCase.execute(command.toCreateQuestionGroupCommand())
            QuestionCreationResult(
                questionId = QuestionId(result.questionId),
                questionItemIds = result.questionItemIds.map { QuestionItemId(it) },
            )
        }

    private fun QuestionCreationCommand.toCreateQuestionGroupCommand(): CreateQuestionCommand =
        CreateQuestionCommand(
            generationId = generationId.value,
            sharedContext = result.sharedContext?.toDomain(),
            metadata = QuestionMetadata(
                subject = metadata.subject,
                questionType = metadata.questionType,
                questionSubType = metadata.questionSubType,
                difficulty = metadata.difficulty,
                passageTopic = toPassageTopic(),
            ),
            questions = result.questions.map { q ->
                CreateQuestionCommand.QuestionCommand(
                    stem = q.stem,
                    exhibit = q.exhibit?.toDomain(),
                    answerSheet = q.toAnswerSheet(),
                    explanation = q.explanation.toDomain(),
                )
            },
        )

    // ── LlmSharedContextResult → SharedQuestionContext ────────
    private fun LlmSharedContextResult.toDomain(): SharedQuestionContext =
        SharedQuestionContext.Text(
            content = content,
            description = description,
        )

    // ── LlmExhibitResult → Exhibit ────────────────────────────
    private fun LlmExhibitResult.toDomain(): Exhibit =
        when (this) {
            is LlmExhibitResult.Proposition ->
                Exhibit.PropositionExhibit(
                    propositions = propositions.map { p ->
                        Proposition(label = p.label, content = p.content)
                    }
                )
            is LlmExhibitResult.Text ->
                Exhibit.TextExhibit(content = content)
        }

    // ── LlmQuestionResult → AnswerSheet ───────────────────────
    private fun LlmQuestionResult.toAnswerSheet(): AnswerSheet {
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

    // ── PassageTopic ──────────────────────────────────────────
    private fun QuestionCreationCommand.toPassageTopic(): PassageTopic =
        PassageTopic(
            category = metadata.topicCategory,
            keyword = metadata.topicKeyword,
        )
}
