package com.examhelper.api.exam.application.handler

import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.kernel.event.GenerationCompletedEvent
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionGenerationCompletedEventHandler(
    private val examStore: ExamStore
) {
    //@EventListener
    //@Transactional
    fun handle(event: GenerationCompletedEvent) {
        val exam = examStore.loadByGenerationId(QuestionGenerationId(event.generationId)) ?: return

        exam.completeGeneration(
            ExamGenerationResult(
                generationId = QuestionGenerationId(event.generationId),
                successCount = event.successCount,
                failCount = event.failureCount,
            )
        )

        examStore.save(exam)
    }
}
