package com.examhelper.api.kernel.core

interface DomainEventPublisher {
    fun publish(event: DomainEvent)
    fun publishAll(events: List<DomainEvent>)
    fun <T: Identifier<*>> publishFrom(aggregateRoot: AggregateRoot<T>)
}
