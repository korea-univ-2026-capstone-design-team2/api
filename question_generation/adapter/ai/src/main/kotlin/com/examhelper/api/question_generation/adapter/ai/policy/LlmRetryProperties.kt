package com.examhelper.api.question_generation.adapter.ai.policy

import com.examhelper.api.infrastructure.retry.RetryPolicy
import com.examhelper.api.question_generation.adapter.ai.exception.LlmGenerationException
import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.math.pow

@ConfigurationProperties(prefix = "llm.retry")
data class LlmRetryProperties(
    val llm: LlmCallRetry = LlmCallRetry(),
) {
    data class LlmCallRetry(
        override val maxAttempts: Int = 3,
        val initialBackoffMillis: Long = 500L,
        val backoffMultiplier: Double = 2.0,
        val maxBackoffMillis: Long = 10_000L,
    ): RetryPolicy {
        init {
            require(maxAttempts in 1..10) {
                "llm.retry.llm.max-attempts는 1~10 사이여야 합니다: $maxAttempts"
            }
            require(initialBackoffMillis > 0) {
                "llm.retry.llm.initial-backoff-millis는 양수여야 합니다: $initialBackoffMillis"
            }
            require(backoffMultiplier >= 1.0) {
                "llm.retry.llm.backoff-multiplier는 1.0 이상이어야 합니다: $backoffMultiplier"
            }
            require(maxBackoffMillis >= initialBackoffMillis) {
                "llm.retry.llm.max-backoff-millis는 initial-backoff-millis 이상이어야 합니다"
            }
        }

        override fun backoffMillis(attempt: Int): Long =
            (initialBackoffMillis * backoffMultiplier.pow(attempt.toDouble()))
                .toLong()
                .coerceAtMost(maxBackoffMillis)

        override fun isRetryable(ex: Throwable): Boolean =
            ex is LlmGenerationException.Retryable
    }
}
