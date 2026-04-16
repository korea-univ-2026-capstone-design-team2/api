package com.examhelper.api.infrastructure.util

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "snowflake")
data class SnowflakeProperties (
    var datacenterId: Long = 0,
    var workerId: Long = 0
)
