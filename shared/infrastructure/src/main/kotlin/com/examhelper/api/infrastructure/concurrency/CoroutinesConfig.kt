package com.examhelper.api.infrastructure.concurrency

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class CoroutinesConfig {
    private val applicationJob = SupervisorJob()

    @Bean
    fun questionGenerationDispatcher(): CoroutineDispatcher =
        Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()

    @Bean
    fun applicationScope(): CoroutineScope =
        CoroutineScope(applicationJob + Dispatchers.IO)

    @PreDestroy
    fun destroy() {
        applicationJob.cancel()
    }
}
