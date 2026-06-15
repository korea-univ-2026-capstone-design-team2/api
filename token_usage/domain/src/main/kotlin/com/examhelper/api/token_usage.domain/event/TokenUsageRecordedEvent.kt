package com.examhelper.api.token_usage.domain.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.EventChannel
import java.math.BigDecimal
import java.time.Instant

class TokenUsageRecordedEvent(
    val tokenUsageId: Long,
    val memberId: Long,
    val provider: String,
    val model: String,
    val targetType: String,
    val targetReferenceId: Long,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val totalCost: BigDecimal,
    val currency: String,
    val status: String,
    val occurredAt: Instant
) : DomainEvent(
    aggregateId = tokenUsageId.toString(),
    aggregateType = "TokenUsage",
    channel = EventChannel.INTERNAL,
) {
    override val eventType: String =
        "TokenUsageRecorded"

    override fun topic(): String =
        "token-usage.recorded"
}
