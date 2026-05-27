package com.examhelper.api.token_usage.port.outbound

import com.examhelper.api.kernel.identifier.TokenUsageId
import com.examhelper.api.token_usage.domain.TokenUsage

interface TokenUsageStore {
    fun save(tokenUsage: TokenUsage)
    fun saveAll(tokenUsages: List<TokenUsage>)
    fun loadById(id: TokenUsageId): TokenUsage?
}
