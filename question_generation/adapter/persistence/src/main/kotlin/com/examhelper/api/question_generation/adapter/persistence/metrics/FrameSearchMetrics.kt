package com.examhelper.api.question_generation.adapter.persistence.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component

@Component
class FrameSearchMetrics(
    meterRegistry: MeterRegistry
) {

    val searchTimer: Timer =
        Timer.builder("frame.search.duration")
            .description("Frame search latency")
            .publishPercentileHistogram()
            .register(meterRegistry)
}
