package com.examhelper.api.exam.application.handler

import com.examhelper.api.exam.domain.entity.ExamItem
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.event.QuestionGeneratedEvent
import com.examhelper.api.kernel.identifier.ExamItemId
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionGeneratedEventHandler(
    private val examStore: ExamStore,
    private val idGenerator: IdGenerator
) {
    //@EventListener
    //@Transactional
    fun handle(event: QuestionGeneratedEvent) {
        val exam = examStore.loadByGenerationId(QuestionGenerationId(event.generationId)) ?: return

        exam.addItem(
            ExamItem(
                id = ExamItemId(idGenerator.generateId()),
                questionId = QuestionId(event.questionId),
                ordering = event.ordering,
            )
        )

        examStore.save(exam)
    }
}
