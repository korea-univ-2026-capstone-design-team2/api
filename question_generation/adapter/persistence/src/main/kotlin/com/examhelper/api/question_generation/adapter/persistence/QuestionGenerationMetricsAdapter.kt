package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.question_generation.port.outbound.QuestionGenerationMetricsPort
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class QuestionGenerationMetricsAdapter(
    private val meterRegistry: MeterRegistry
) : QuestionGenerationMetricsPort {

    private val timer = Timer.builder(
        "question.generation.total.duration"
    )
        .publishPercentileHistogram()
        .register(meterRegistry)

    override fun recordTotalDuration(
        durationMs: Long
    ) {
        timer.record(durationMs, TimeUnit.MILLISECONDS)
    }
}
