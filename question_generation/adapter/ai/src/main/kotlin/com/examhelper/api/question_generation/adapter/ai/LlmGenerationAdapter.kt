package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.question_generation.adapter.ai.dto.LlmGenerationResponse
import com.examhelper.api.question_generation.adapter.ai.exception.LlmGenerationException
import com.examhelper.api.question_generation.adapter.ai.mapper.LlmGenerationResponseMapper
import com.examhelper.api.question_generation.adapter.ai.metrics.LlmGenerationMetrics
import com.examhelper.api.question_generation.adapter.ai.policy.LlmRetryProperties
import com.examhelper.api.question_generation.port.outbound.LlmGenerationPort
import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import mu.KotlinLogging
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
    private val responseMapper: LlmGenerationResponseMapper,
    private val objectMapper: ObjectMapper,
    resourceLoader: ResourceLoader,
    private val metrics: LlmGenerationMetrics,
    private val meterRegistry: MeterRegistry,
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
                retryable(retryProperties.llm) {
                    val userPrompt = promptAssembler.assembleUserPrompt(command)
                    val rawJson = callLlm(userPrompt)
                    val response = parseResponse(rawJson)
                    responseMapper.toDomain(response)
                }
            } finally {
                sample.stop(metrics.generationTimer)
            }
        }
    }

    private fun callLlm(userPrompt: String): String =
        try {
            val prompt = Prompt(SystemMessage(systemPrompt), UserMessage(userPrompt))
            chatModel.call(prompt).result?.output?.text
                ?: throw LlmGenerationException.Retryable.EmptyResponse()
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

    suspend fun <T> retryable(
        policy: LlmRetryProperties.LlmCallRetry,
        block: suspend (attempt: Int) -> T
    ): T {
        var lastException: LlmGenerationException.Retryable? = null

        repeat(policy.maxAttempts) { attemptIndex ->
            try {
                return block(attemptIndex)
            } catch (ex: LlmGenerationException.Retryable) {
                lastException = ex
                val delayMillis = when (ex) {
                    is LlmGenerationException.Retryable.RateLimited ->
                        ex.retryAfterMillis ?: policy.backoffMillis(attemptIndex)
                    else -> policy.backoffMillis(attemptIndex)
                }
                if (attemptIndex < policy.maxAttempts - 1) {
                    delay(delayMillis)
                }
            }
        }

        throw lastException ?: IllegalStateException("재시도 정책 오류")
    }
}
