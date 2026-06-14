package com.examhelper.api.exam.application

import com.examhelper.api.exam.domain.entity.ExamItem
import com.examhelper.api.exam.domain.exception.ExamException
import com.examhelper.api.exam.port.inbound.AddExamItemUseCase
import com.examhelper.api.exam.port.inbound.command.AddExamItemCommand
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.ExamItemId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddExamItemService(
    private val examStore: ExamStore,
    private val idGenerator: IdGenerator,
) : AddExamItemUseCase {
    @Transactional
    override fun execute(command: AddExamItemCommand) {
        val exam = examStore.loadByGenerationId(command.generationId)
            ?: throw ExamException.NotFound(command.generationId.value)

        exam.addItem(
            ExamItem(
                id = ExamItemId(idGenerator.generateId()),
                questionId = command.questionId,
                ordering = command.ordering
            )
        )

        examStore.save(exam)
    }
}
