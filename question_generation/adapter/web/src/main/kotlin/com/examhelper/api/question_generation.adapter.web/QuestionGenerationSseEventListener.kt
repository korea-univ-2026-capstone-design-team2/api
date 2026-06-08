package com.examhelper.api.question_generation.adapter.web

import com.examhelper.api.question_generation.adapter.web.response.QuestionGeneratedSseResponse
import com.examhelper.api.question_generation.domain.event.GenerationCompletedEvent
import com.examhelper.api.question_generation.domain.event.GenerationFailedEvent
import com.examhelper.api.question_generation.domain.event.QuestionGeneratedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class QuestionGenerationSseEventListener(
    private val registry: QuestionGenerationEmitterRegistry
) {
    @EventListener
    fun handle(event: QuestionGeneratedEvent) {
        registry.send(
            generationId = event.generationId,
            eventName = "question-generated",
            data = QuestionGeneratedSseResponse(
                generationId = event.generationId.toString(),
                questionId = event.questionId.toString()
            )
        )
    }

    @EventListener
    fun handle(event: GenerationCompletedEvent) {
        registry.send(
            generationId = event.generationId,
            eventName = "generation-completed",
            data = mapOf(
                "generationId" to event.generationId.toString(),
                "successCount" to event.successCount,
                "failureCount" to event.failureCount,
            )
        )
    }

    @EventListener
    fun handle(event: GenerationFailedEvent) {
        registry.send(
            generationId = event.generationId,
            eventName = "generation-failed",
            data = mapOf(
                "generationId" to event.generationId.toString(),
                "reason" to event.reason,
            )
        )
    }
}
