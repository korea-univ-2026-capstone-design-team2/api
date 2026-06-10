package com.examhelper.api.question_generation.adapter.ai.mapper

import com.examhelper.api.kernel.type.PropositionLabel
import com.examhelper.api.question_generation.adapter.ai.dto.LlmGenerationResponse
import com.examhelper.api.question_generation.adapter.ai.exception.LlmGenerationMappingException
import com.examhelper.api.question_generation.port.outbound.result.LlmChoiceResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExhibitResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExplanationResult
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult
import com.examhelper.api.question_generation.port.outbound.result.LlmPropositionResult
import com.examhelper.api.question_generation.port.outbound.result.LlmQuestionResult
import com.examhelper.api.question_generation.port.outbound.result.LlmSharedContextResult
import org.springframework.stereotype.Component

@Component
class LlmGenerationResponseMapper {
    fun toDomain(response: LlmGenerationResponse): LlmGenerationResult {
        require(response.questions.isNotEmpty()) {
            throw LlmGenerationMappingException.EmptyQuestions()
        }

        return LlmGenerationResult(
            sharedContext = response.sharedContext?.toDomain(),
            questions = response.questions.map { it.toDomain() },
        )
    }

    // ── SharedContext ─────────────────────────────────────────
    private fun LlmGenerationResponse.LlmSharedContextResponse.toDomain(): LlmSharedContextResult? {
        val resolvedContent = content?.takeIf { it.isNotBlank() } ?: return null
        return LlmSharedContextResult(
            content = resolvedContent,
            description = description?.takeIf { it.isNotBlank() },
        )
    }

    // ── Question ──────────────────────────────────────────────
    private fun LlmGenerationResponse.LlmQuestionResponse.toDomain(): LlmQuestionResult {
        validateChoices(choices)
        return LlmQuestionResult(
            stem = stem,
            exhibit = exhibit?.toDomain(),
            choices = choices.map { it.toDomain() },
            explanation = explanation.toDomain(expectedChoiceCount = choices.size),
        )
    }

    private fun validateChoices(choices: List<LlmGenerationResponse.LlmChoiceResponse>) {
        val correctCount = choices.count { it.isCorrect }
        if (correctCount == 0) throw LlmGenerationMappingException.NoCorrectChoice()
        if (correctCount > 1)  throw LlmGenerationMappingException.MultipleCorrectChoices(correctCount)
    }

    // ── Exhibit ───────────────────────────────────────────────

    private fun LlmGenerationResponse.LlmExhibitResponse.toDomain(): LlmExhibitResult? =
        when (type?.uppercase()) {
            "PROPOSITION" -> toPropositionExhibit()
            "TEXT"        -> toTextExhibit()
            null, "NULL", "" -> null
            else -> throw LlmGenerationMappingException.UnknownExhibitType(type)
        }

    private fun LlmGenerationResponse.LlmExhibitResponse.toPropositionExhibit(): LlmExhibitResult.Proposition {
        val resolved = propositions
            ?.takeIf { it.isNotEmpty() }
            ?.map { it.toDomain() }
            ?: throw LlmGenerationMappingException.MissingPropositions()
        return LlmExhibitResult.Proposition(propositions = resolved)
    }

    private fun LlmGenerationResponse.LlmExhibitResponse.toTextExhibit(): LlmExhibitResult.Text {
        val resolved = content?.takeIf { it.isNotBlank() }
            ?: throw LlmGenerationMappingException.MissingTextContent()
        return LlmExhibitResult.Text(content = resolved)
    }

    private fun LlmGenerationResponse.LlmPropositionResponse.toDomain(): LlmPropositionResult =
        LlmPropositionResult(
            label = label.toPropositionLabel(),
            content = content
        )

    // ── Choice ────────────────────────────────────────────────

    private fun LlmGenerationResponse.LlmChoiceResponse.toDomain(): LlmChoiceResult =
        when (type.uppercase()) {
            "TEXT"                   -> toTextChoice()
            "PROPOSITION_COMBINATION" -> toPropositionCombinationChoice()
            else -> throw LlmGenerationMappingException.UnknownChoiceType(type)
        }

    private fun LlmGenerationResponse.LlmChoiceResponse.toTextChoice(): LlmChoiceResult.Text =
        LlmChoiceResult.Text(
            number = number,
            isCorrect = isCorrect,
            content = content
                ?: throw LlmGenerationMappingException.MissingChoiceContent(number),
        )

    private fun LlmGenerationResponse.LlmChoiceResponse.toPropositionCombinationChoice(): LlmChoiceResult.PropositionCombination =
        LlmChoiceResult.PropositionCombination(
            number = number,
            isCorrect = isCorrect,
            labels = labels
                ?.takeIf { it.isNotEmpty() }
                ?.map { it.toPropositionLabel(choiceNumber = number) }
                ?: throw LlmGenerationMappingException.MissingChoiceLabels(number),
        )

    // ── Explanation ───────────────────────────────────────────

    private fun LlmGenerationResponse.LlmExplanationResponse.toDomain(expectedChoiceCount: Int): LlmExplanationResult {
        val parsedReasons = parseIncorrectReasons()
        validateIncorrectReasonKeys(parsedReasons.keys, expectedChoiceCount)
        return LlmExplanationResult(
            correctReason = correctReason,
            incorrectReasons = parsedReasons,
        )
    }

    private fun LlmGenerationResponse.LlmExplanationResponse.parseIncorrectReasons(): Map<Int, String> =
        incorrectReasons.mapKeys { (key, _) ->
            key.toIntOrNull()
                ?: throw LlmGenerationMappingException.InvalidExplanationKey(key)
        }

    private fun validateIncorrectReasonKeys(keys: Set<Int>, expectedChoiceCount: Int) {
        val missingKeys = (1..expectedChoiceCount).toSet() - keys
        require(missingKeys.isEmpty()) {
            throw LlmGenerationMappingException.MissingExplanationKeys(missingKeys)        }
    }

    // ── 공통 유틸 ─────────────────────────────────────────────

    private fun String.toPropositionLabel(choiceNumber: Int? = null): PropositionLabel =
        runCatching { PropositionLabel.valueOf(this) }
            .getOrElse {
                throw when (choiceNumber) {
                    null -> LlmGenerationMappingException.InvalidPropositionLabel(this)
                    else -> LlmGenerationMappingException.InvalidPropositionLabelInChoice(this, choiceNumber)
                }
            }
}
