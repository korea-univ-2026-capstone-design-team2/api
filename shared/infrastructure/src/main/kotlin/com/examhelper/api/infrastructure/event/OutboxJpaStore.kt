package com.examhelper.api.infrastructure.event

import org.springframework.data.jpa.repository.JpaRepository

interface OutboxJpaStore : JpaRepository<OutboxEntity, String>
