package com.examhelper.api.infrastructure.event

import com.examhelper.api.kernel.core.DomainEvent
import org.springframework.stereotype.Component

@Component
class ExternalEventPublisher {
    fun publish(event: DomainEvent) {
        // TODO: 추후 필요할지 검토
    }
}
