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
                .trim()
                .replace(Regex("""^```[a-zA-Z]*\s*""", RegexOption.MULTILINE), "")
                .replace(Regex("""```\s*$""", RegexOption.MULTILINE), "")
                .trim()

            objectMapper.readValue(cleaned, LlmQuestionResponse::class.java)
        } catch (ex: Exception) {
            throw LlmGenerationException.ResponseParseFailed(ex)
        }

    // ── LlmQuestionResponse → LlmGenerationResult 변환 ────────
    private fun LlmQuestionResponse.toDomain(): LlmGenerationResult {
        val correctChoices = choices.filter { it.isCorrect }
        when {
            correctChoices.isEmpty() ->
                throw LlmGenerationException.InvalidChoice("정답(isCorrect=true)이 없습니다")

            correctChoices.size > 1 ->
                throw LlmGenerationException.InvalidChoice(
                    "정답이 ${correctChoices.size}개입니다. 반드시 1개여야 합니다"
                )
        }

        return LlmGenerationResult(
            stem = stem,
            passage = passage?.toDomain(),
            exhibit = exhibit?.toDomain(),
            choices = choices.map { it.toDomain() },
            explanation = explanation.toDomain(choices.size),
        )
    }

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
                            label = runCatching { PropositionLabel.valueOf(p.label) }
                                .getOrElse {
                                    throw LlmGenerationException.InvalidExhibit(
                                        "유효하지 않은 PropositionLabel: '${p.label}'"
                                    )
                                },
                            content = p.content,
                        )
                    }
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw LlmGenerationException.InvalidExhibit(
                        "PROPOSITION exhibit에 propositions가 없습니다"
                    )
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
                labels =  labels
                    ?.map { label ->
                        runCatching { PropositionLabel.valueOf(label) }
                            .getOrElse {
                                throw LlmGenerationException.InvalidChoice(
                                    "유효하지 않은 PropositionLabel: '$label' (선지 $number)"
                                )
                            }
                    }
                    ?.takeIf { it.isNotEmpty() }
                    ?: throw LlmGenerationException.InvalidChoice(
                        "PROPOSITION_COMBINATION 선지에 labels가 없습니다: $number"
                    )
            )
            else -> throw LlmGenerationException.InvalidChoice("알 수 없는 choice type: $type")
        }

    private fun LlmQuestionResponse.LlmExplanationResponse.toDomain(expectedChoiceCount: Int): LlmExplanationResult {
        val parsedKeys = incorrectReasons.mapKeys { (k, _) ->
            k.toIntOrNull()
                ?: throw LlmGenerationException.InvalidExplanation(
                    "incorrectReasons key가 숫자가 아닙니다: $k"
                )
        }

        val requiredKeys = (1..expectedChoiceCount).toSet()
        val missingKeys = requiredKeys - parsedKeys.keys
        if (missingKeys.isNotEmpty()) {
            throw LlmGenerationException.InvalidExplanation(
                "incorrectReasons에 필수 키가 누락되었습니다: $missingKeys"
            )
        }

        return LlmExplanationResult(
            correctReason = correctReason,
            incorrectReasons = parsedKeys,
        )
    }
}
