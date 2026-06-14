package com.examhelper.api.infrastructure.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.OutboxStore
import org.springframework.stereotype.Component

@Component
class ExternalEventPublisher(
    private val outboxStore: OutboxStore
) {
    fun publish(event: DomainEvent) {
        outboxStore.save(event)
    }
}
