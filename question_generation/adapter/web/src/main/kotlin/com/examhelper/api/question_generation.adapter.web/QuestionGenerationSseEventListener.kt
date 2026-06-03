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
                generationId = event.generationId,
                questionId = event.questionId
            )
        )
    }

    @EventListener
    fun handle(event: GenerationCompletedEvent) {
        registry.send(
            generationId = event.generationId,
            eventName = "generation-completed",
            data = event
        )
    }

    @EventListener
    fun handle(event: GenerationFailedEvent, ) {
        registry.send(
            generationId = event.generationId,
            eventName = "generation-failed",
            data = event
        )
    }
}
