package com.examhelper.api.question_generation.adapter.web

import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Component
class QuestionGenerationEmitterRegistry {
    private val emitters = ConcurrentHashMap<Long, SseEmitter>()

    fun connect(generationId: Long): SseEmitter {
        val emitter = SseEmitter(60 * 60 * 1000L)

        emitter.onCompletion { emitters.remove(generationId) }
        emitter.onTimeout { emitters.remove(generationId) }
        emitter.onError { emitters.remove(generationId) }

        emitters.put(generationId, emitter)?.let { runCatching { it.complete() } }

        return emitter
    }

    fun send(
        generationId: Long,
        eventName: String,
        data: Any,
    ) {
        val emitter = emitters[generationId] ?: return

        try {
            emitter.send(
                SseEmitter.event()
                    .name(eventName)
                    .data(data)
            )

            if (eventName == "generation-completed" || eventName == "generation-failed") {
                emitters.remove(generationId)
                runCatching { emitter.complete() }
            }
        } catch (_: Exception) {
            emitters.remove(generationId)
        }
    }
}
