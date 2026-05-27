package com.examhelper.api.token_usage.adapter.persistence

import com.examhelper.api.kernel.identifier.TokenUsageId
import com.examhelper.api.token_usage.domain.TokenUsage
import com.examhelper.api.token_usage.port.outbound.TokenUsageStore
import org.springframework.stereotype.Repository

@Repository
class TokenUsagePersistenceAdapter(
    private val jpaStore: TokenUsageJpaStore,
) : TokenUsageStore {
    override fun save(tokenUsage: TokenUsage) {
        val entity = TokenUsageEntity.fromDomain(tokenUsage)
        jpaStore.save(entity)
    }

    override fun saveAll(tokenUsages: List<TokenUsage>) {
        val entities = tokenUsages.map(TokenUsageEntity::fromDomain)
        jpaStore.saveAll(entities)
    }

    override fun loadById(id: TokenUsageId): TokenUsage? {
        return jpaStore.findById(id.value).orElse(null)?.toDomain()
    }
}
