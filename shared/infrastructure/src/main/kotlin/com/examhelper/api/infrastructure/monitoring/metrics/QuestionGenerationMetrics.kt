package com.examhelper.api.infrastructure.monitoring.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component

@Component
class QuestionGenerationMetrics(
    meterRegistry: MeterRegistry
) {
    val frameSearchTimer: Timer =
        Timer.builder(MetricNames.FRAME_SEARCH_DURATION)
            .publishPercentileHistogram()
            .register(meterRegistry)

    val llmGenerationTimer: Timer =
        Timer.builder(MetricNames.LLM_GENERATION_DURATION)
            .publishPercentileHistogram()
            .register(meterRegistry)

    val questionCreationTimer: Timer =
        Timer.builder(MetricNames.QUESTION_CREATION_DURATION)
            .publishPercentileHistogram()
            .register(meterRegistry)

    val totalGenerationTimer: Timer =
        Timer.builder(MetricNames.QUESTION_GENERATION_TOTAL_DURATION)
            .publishPercentileHistogram()
            .register(meterRegistry)
}
