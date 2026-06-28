package com.examhelper.api.exam.application

import com.examhelper.api.exam.domain.exception.ExamException
import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.port.inbound.CompleteExamGenerationUseCase
import com.examhelper.api.exam.port.inbound.command.CompleteExamGenerationCommand
import com.examhelper.api.exam.port.outbound.ExamStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompleteExamGenerationService(
    private val examStore: ExamStore
) : CompleteExamGenerationUseCase {
    @Transactional
    override fun execute(command: CompleteExamGenerationCommand) {
        val exam = examStore.loadByGenerationIdForUpdate(command.generationId)
            ?: throw ExamException.NotFoundByGenerationId(command.generationId.value)

        exam.markGenerationFinished(
            ExamGenerationResult(
                generationId = command.generationId,
                successCount = command.successCount,
                failCount = command.failCount,
            )
        )

        examStore.save(exam)
    }
}
