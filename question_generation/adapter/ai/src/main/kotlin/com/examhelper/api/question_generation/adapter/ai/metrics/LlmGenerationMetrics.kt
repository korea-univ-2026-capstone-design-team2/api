package com.examhelper.api.question_generation.adapter.ai.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component

@Component
class LlmGenerationMetrics(
    meterRegistry: MeterRegistry
) {
    val generationTimer: Timer =
        Timer.builder("llm.generate.duration")
            .description("LLM generation latency")
            .publishPercentileHistogram()
            .register(meterRegistry)
}
