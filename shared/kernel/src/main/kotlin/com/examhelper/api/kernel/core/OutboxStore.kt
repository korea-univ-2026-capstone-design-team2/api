package com.examhelper.api.kernel.core

interface OutboxStore {
    fun save(event: DomainEvent)
}
