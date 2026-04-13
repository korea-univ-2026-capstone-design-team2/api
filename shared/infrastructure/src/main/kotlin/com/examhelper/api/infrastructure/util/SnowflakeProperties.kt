package com.examhelper.api.infrastructure.util

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "snowflake")
data class SnowflakeProperties (
    var datacenterId: Long = 0,
    var workerId: Long = 0
)
