package com.examhelper.api.kernel.core

import com.examhelper.api.kernel.identifier.DomainEventId
import java.time.Instant
import java.util.UUID

abstract class DomainEvent(
    val aggregateId: String,
    val aggregateType: String,
    val id: DomainEventId = DomainEventId(UUID.randomUUID().toString()),
    val createdAt: Instant = Instant.now(),
    val channel: EventChannel = EventChannel.INTERNAL,
) {
    abstract val eventType: String
    abstract fun topic(): String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DomainEvent) return false
        return id == other.id
    }
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String =
        "${this::class.simpleName}(" +
                "id=$id, " +
                "aggregateType=$aggregateType, " +
                "eventType=$eventType, " +
                "channel=$channel, " +
                "createdAt=$createdAt)"
}
