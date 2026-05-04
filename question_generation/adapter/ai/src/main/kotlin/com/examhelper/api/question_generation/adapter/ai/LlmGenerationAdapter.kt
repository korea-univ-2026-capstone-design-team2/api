package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.kernel.type.PropositionLabel
import com.examhelper.api.question_generation.adapter.ai.dto.LlmQuestionResponse
import com.examhelper.api.question_generation.adapter.ai.exception.LlmGenerationException
import com.examhelper.api.question_generation.port.outbound.LlmGenerationPort
import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.result.LlmChoiceResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExhibitResult
import com.examhelper.api.question_generation.port.outbound.result.LlmExplanationResult
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult
import com.examhelper.api.question_generation.port.outbound.result.LlmPassageResult
import com.examhelper.api.question_generation.port.outbound.result.LlmPropositionResult
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class LlmGenerationAdapter(
    private val chatModel: ChatModel,
    private val promptAssembler: PromptAssembler,
    private val objectMapper: ObjectMapper,
    resourceLoader: ResourceLoader,
) : LlmGenerationPort {
    private val systemPrompt: String by lazy {
        resourceLoader
            .getResource("classpath:prompts/question-generation.system.prompt")
            .getContentAsString(Charsets.UTF_8)
    }

    override fun generate(command: LlmGenerationCommand): LlmGenerationResult {
        val userPrompt = promptAssembler.assembleUserPrompt(command)

        val rawJson = try {
            val prompt = Prompt(
                listOf(
                    SystemMessage(systemPrompt),
                    UserMessage(userPrompt),
                )
            )
            chatModel.call(prompt)
                .result
                .output
                .text
                ?: throw LlmGenerationException.EmptyResponse()
        } catch (ex: LlmGenerationException) {
            throw ex
        } catch (ex: Exception) {
            throw LlmGenerationException.ApiCallFailed(ex)
        }

        val response = parseResponse(rawJson)
        return response.toDomain()
    }

    // ── JSON 파싱 ─────────────────────────────────────────────
    private fun parseResponse(rawJson: String): LlmQuestionResponse =
        try {
            val cleaned = rawJson
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            objectMapper.readValue(cleaned, LlmQuestionResponse::class.java)
        } catch (ex: Exception) {
            throw LlmGenerationException.ResponseParseFailed(ex)
        }

    // ── LlmQuestionResponse → LlmGenerationResult 변환 ────────
    private fun LlmQuestionResponse.toDomain(): LlmGenerationResult =
        LlmGenerationResult(
            stem = stem,
            passage = passage?.toDomain(),
            exhibit = exhibit?.toDomain(),
            choices = choices.map { it.toDomain() },
            explanation = explanation.toDomain(),
        )

    private fun LlmQuestionResponse.LlmPassageResponse.toDomain(): LlmPassageResult? {
        val c = content?.takeIf { it.isNotBlank() } ?: return null
        return LlmPassageResult(
            content = c,
            description = description?.takeIf { it.isNotBlank() },
        )
    }

    private fun LlmQuestionResponse.LlmExhibitResponse.toDomain(): LlmExhibitResult? =
        when (type?.uppercase()) {
            "PROPOSITION" -> {
                val props = propositions
                    ?.map { p ->
                        LlmPropositionResult(
                            label = PropositionLabel.valueOf(p.label),
                            content = p.content,
                        )
                    }
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw LlmGenerationException.InvalidExhibit("PROPOSITION exhibit에 propositions가 없습니다")

                LlmExhibitResult.Proposition(propositions = props)
            }

            "TEXT" -> {
                val c = content?.takeIf { it.isNotBlank() }
                    ?: throw LlmGenerationException.InvalidExhibit("TEXT exhibit에 content가 없습니다")
                LlmExhibitResult.Text(content = c)
            }

            null, "NULL", "" -> null
            else -> throw LlmGenerationException.InvalidExhibit("알 수 없는 exhibit type: $type")
        }

    private fun LlmQuestionResponse.LlmChoiceResponse.toDomain(): LlmChoiceResult =
        when (type.uppercase()) {
            "TEXT" -> LlmChoiceResult.Text(
                number = number,
                isCorrect = isCorrect,
                content = content
                    ?: throw LlmGenerationException.InvalidChoice("TEXT 선지에 content가 없습니다: $number"),
            )

            "PROPOSITION_COMBINATION" -> LlmChoiceResult.PropositionCombination(
                number = number,
                isCorrect = isCorrect,
                labels = labels
                    ?.map { PropositionLabel.valueOf(it) }
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw LlmGenerationException.InvalidChoice("PROPOSITION_COMBINATION 선지에 labels가 없습니다: $number"),
            )

            else -> throw LlmGenerationException.InvalidChoice("알 수 없는 choice type: $type")
        }

    private fun LlmQuestionResponse.LlmExplanationResponse.toDomain(): LlmExplanationResult =
        LlmExplanationResult(
            correctReason = correctReason,
            incorrectReasons = incorrectReasons
                .mapKeys { (k, _) ->
                    k.toIntOrNull()
                        ?: throw LlmGenerationException.InvalidExplanation("incorrectReasons key가 숫자가 아닙니다: $k")
                },
        )
}
