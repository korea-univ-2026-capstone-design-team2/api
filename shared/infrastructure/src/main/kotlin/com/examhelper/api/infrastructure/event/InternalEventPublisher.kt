package com.examhelper.api.infrastructure.event

import com.examhelper.api.kernel.core.DomainEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class InternalEventPublisher(
    private val publisher: ApplicationEventPublisher,
) {
    fun publish(event: DomainEvent) {
        publisher.publishEvent(event)
    }
}
