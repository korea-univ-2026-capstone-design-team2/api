package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.infrastructure.retry.RetryExecutor
import com.examhelper.api.question_generation.adapter.ai.dto.LlmGenerationResponse
import com.examhelper.api.question_generation.adapter.ai.exception.LlmGenerationException
import com.examhelper.api.question_generation.adapter.ai.mapper.LlmGenerationResponseMapper
import com.examhelper.api.question_generation.adapter.ai.mapper.LlmTokenUsageMapper
import com.examhelper.api.question_generation.adapter.ai.metrics.LlmGenerationMetrics
import com.examhelper.api.question_generation.adapter.ai.policy.LlmRetryProperties
import com.examhelper.api.question_generation.port.outbound.LlmGenerationPort
import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.metadata.Usage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class LlmGenerationAdapter(
    private val chatModel: ChatModel,
    private val promptAssembler: PromptAssembler,
    private val responseMapper: LlmGenerationResponseMapper,
    private val tokenUsageMapper: LlmTokenUsageMapper,
    private val objectMapper: ObjectMapper,
    resourceLoader: ResourceLoader,
    private val metrics: LlmGenerationMetrics,
    private val meterRegistry: MeterRegistry,
    private val retryExecutor: RetryExecutor,
    private val retryProperties: LlmRetryProperties
) : LlmGenerationPort {
    private val logger = KotlinLogging.logger {}

    private val systemPrompt: String = resourceLoader
        .getResource("classpath:prompts/question-generation.system.prompt")
        .getContentAsString(Charsets.UTF_8)

    override suspend fun generate(command: LlmGenerationCommand): LlmGenerationResult {
        return withContext(Dispatchers.IO) {
            val sample = Timer.start(meterRegistry)
            try {
                retryExecutor.execute(retryProperties.llm) {
                    val userPrompt = promptAssembler.assembleUserPrompt(command)
                    val llmCallResult = callLlm(userPrompt)
                    val response = parseResponse(llmCallResult.rawJson)
                    val usage = tokenUsageMapper.toDomain("gpt-5-mini", llmCallResult.usage)
                    responseMapper.toDomain(response, usage)
                }
            } finally {
                sample.stop(metrics.generationTimer)
            }
        }
    }

    private fun callLlm(userPrompt: String): LlmCallResult =
        try {
            val prompt = Prompt(
                SystemMessage(systemPrompt),
                UserMessage(userPrompt)
            )

            val response = chatModel.call(prompt)

            LlmCallResult(
                rawJson = response.result?.output?.text
                    ?: throw LlmGenerationException.Retryable.EmptyResponse(),
                usage = response.metadata.usage
            )

        } catch (ex: LlmGenerationException) {
            throw ex
        } catch (ex: Exception) {
            logger.warn(ex) { "LLM API 호출 실패, 재시도 대상으로 분류" }
            throw LlmGenerationException.Retryable.ApiCallFailed(ex)
        }

    private fun parseResponse(rawJson: String): LlmGenerationResponse =
        try {
            val cleaned = rawJson
                .trim()
                .replace(Regex("""^```[a-zA-Z]*\s*""", RegexOption.MULTILINE), "")
                .replace(Regex("""```\s*$""", RegexOption.MULTILINE), "")
                .trim()
            objectMapper.readValue(cleaned, LlmGenerationResponse::class.java)
        } catch (ex: Exception) {
            throw LlmGenerationException.Retryable.ResponseParseFailed(ex)
        }

    private data class LlmCallResult(
        val rawJson: String,
        val usage: Usage
    )
}
