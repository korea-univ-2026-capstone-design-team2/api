package com.examhelper.api.token_usage.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface TokenUsageJpaStore : JpaRepository<TokenUsageEntity, Long>
