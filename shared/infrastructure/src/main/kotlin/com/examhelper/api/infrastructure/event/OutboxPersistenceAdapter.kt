package com.examhelper.api.infrastructure.event

import com.examhelper.api.kernel.core.DomainEvent
import com.examhelper.api.kernel.core.OutboxStore
import org.springframework.stereotype.Repository
import tools.jackson.databind.ObjectMapper

@Repository
class OutboxPersistenceAdapter(
    private val jpaStore: OutboxJpaStore,
    private val objectMapper: ObjectMapper
) : OutboxStore {
    override fun save(event: DomainEvent) {
        val payload = objectMapper.writeValueAsString(event)

        jpaStore.save(
            OutboxEntity.from(
                event = event,
                payload = payload,
            )
        )
    }
}
