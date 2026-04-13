package com.examhelper.api.kernel.core

import java.io.Serializable

abstract class AggregateRoot<ID : Identifier<out Serializable>>(id: ID): DomainEntity<ID>(id) {
    private val domainEvents = mutableListOf<DomainEvent>()
    val domainEventList: List<DomainEvent> get() = domainEvents

    protected fun addDomainEvent(event: DomainEvent) {
        domainEvents.add(event)
    }

    fun clearDomainEvents(): List<DomainEvent> {
        val events = domainEvents.toList()
        domainEvents.clear()
        return events
    }
}
