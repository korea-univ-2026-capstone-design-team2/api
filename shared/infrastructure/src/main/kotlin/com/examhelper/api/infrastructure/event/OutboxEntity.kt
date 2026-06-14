package com.examhelper.api.infrastructure.event

import com.examhelper.api.kernel.core.DomainEvent
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name = "outbox_events",
    indexes = [
        Index(name = "idx_outbox_created_at", columnList = "created_at"),
        Index(name = "idx_outbox_topic", columnList = "topic"),
        Index(name = "idx_outbox_event_type", columnList = "event_type"),
    ]
)
class OutboxEntity(
    @Id
    @Column(nullable = false, length = 36)
    val id: String,

    @Column(nullable = false)
    val aggregateId: String,

    @Column(nullable = false)
    val aggregateType: String,

    @Column(nullable = false)
    val eventType: String,

    @Column(nullable = false)
    val topic: String,

    @Column(nullable = false, columnDefinition = "JSON")
    val payload: String,

    @Column(nullable = false)
    val createdAt: Instant
) {
    companion object {
        fun from(event: DomainEvent, payload: String): OutboxEntity =
            OutboxEntity(
                id = event.id.value,
                aggregateId = event.aggregateId,
                aggregateType = event.aggregateType,
                eventType = event.eventType,
                topic = event.topic(),
                payload = payload,
                createdAt = event.createdAt,
            )
    }
}
