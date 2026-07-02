package com.examhelper.api.infrastructure.retry

interface RetryPolicy {
    val maxAttempts: Int
    fun backoffMillis(attempt: Int): Long
    fun isRetryable(ex: Throwable): Boolean
}
