package com.examhelper.api.infrastructure.util

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.module.kotlin.kotlinModule

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            kotlinModule { JavaTimeModule() }
            registeredModules()
            DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        }
    }
}
