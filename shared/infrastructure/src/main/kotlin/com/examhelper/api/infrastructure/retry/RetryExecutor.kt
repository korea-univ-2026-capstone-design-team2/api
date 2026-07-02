package com.examhelper.api.infrastructure.retry

import kotlinx.coroutines.delay
import org.springframework.stereotype.Component

@Component
class RetryExecutor {
    suspend fun <T> execute(
        policy: RetryPolicy,
        block: suspend () -> T,
    ): T {
        var lastException: Exception? = null

        repeat(policy.maxAttempts) { attempt ->
            try {
                return block()
            } catch (ex: Exception) {
                if (!policy.isRetryable(ex)) throw ex

                lastException = ex

                if (attempt < policy.maxAttempts - 1) {
                    delay(policy.backoffMillis(attempt))
                }
            }
        }

        throw lastException!!
    }
}
