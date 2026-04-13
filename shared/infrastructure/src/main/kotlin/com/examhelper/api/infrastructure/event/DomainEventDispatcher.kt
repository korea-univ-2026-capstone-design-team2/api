package com.examhelper.api.infrastructure.event

import com.examhelper.api.kernel.core.AggregateRoot
import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.DomainEventPublisher
import com.examhelper.api.kernel.core.EventChannel
import com.examhelper.api.kernel.core.Identifier
import org.springframework.stereotype.Component

@Component
class DomainEventDispatcher(
    private val internalPublisher: InternalEventPublisher,
    private val externalPublisher: ExternalEventPublisher,
) : DomainEventPublisher {
    override fun publish(event: DomainEvent) {
        when (event.channel) {
            EventChannel.INTERNAL -> internalPublisher.publish(event)
            EventChannel.EXTERNAL -> externalPublisher.publish(event)
            EventChannel.BOTH -> {
                internalPublisher.publish(event)
                externalPublisher.publish(event)
            }
        }
    }

    override fun publishAll(events: List<DomainEvent>) {
        events.forEach(::publish)
    }

    override fun <T : Identifier<*>> publishFrom(aggregateRoot: AggregateRoot<T>) {
        val events = aggregateRoot.clearDomainEvents()
        publishAll(events)
    }
}
