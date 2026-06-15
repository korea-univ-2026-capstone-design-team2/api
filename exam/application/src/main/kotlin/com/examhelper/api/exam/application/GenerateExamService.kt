package com.examhelper.api.exam.application

import com.examhelper.api.exam.domain.Exam
import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.port.inbound.GenerateExamUseCase
import com.examhelper.api.exam.port.inbound.command.GenerateExamCommand
import com.examhelper.api.exam.port.inbound.result.GenerateExamResult
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.exam.port.outbound.GenerateExamQuestionsPort
import com.examhelper.api.exam.port.outbound.command.GenerateExamQuestionsCommand
import com.examhelper.api.exam.port.outbound.result.GenerateExamQuestionsResult
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.ExamId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GenerateExamService(
    private val examStore: ExamStore,
    private val generateExamQuestionsPort: GenerateExamQuestionsPort,
    private val idGenerator: IdGenerator,
) : GenerateExamUseCase {
    @Transactional
    override fun execute(command: GenerateExamCommand): GenerateExamResult {
        val exam = Exam.create(
            id = ExamId(idGenerator.generateId()),
            memberId = command.memberId,
            title = command.title,
            metadata = command.toMetadata(),
        )
        examStore.save(exam)

        if (exam.isEmptyExam()) return completeEmpty(exam)

        return runCatching { generateExamQuestionsPort.generate(GenerateExamQuestionsCommand(
            memberId = command.memberId,
            metadata = exam.metadata
        )) }
            .fold(
                onSuccess = { result -> startGeneration(exam, result) },
                onFailure = { ex -> failGeneration(exam, ex) },
            )
    }

    private fun completeEmpty(exam: Exam): GenerateExamResult {
        exam.completeGeneration(
            ExamGenerationResult(
                generationId = null,
                successCount = 0,
                failCount = 0,
            )
        )
        examStore.save(exam)

        return exam.toGenerateExamResult()
    }

    private fun startGeneration(exam: Exam, result: GenerateExamQuestionsResult): GenerateExamResult {
        exam.startGeneration(result.generationId)
        examStore.save(exam)

        return exam.toGenerateExamResult()
    }

    private fun failGeneration(exam: Exam, ex: Throwable): GenerateExamResult {
        exam.failGeneration(ex.message ?: "Unknown generation error")
        examStore.save(exam)

        return exam.toGenerateExamResult()
    }
}

private fun Exam.toGenerateExamResult() = GenerateExamResult(
    examId = id,
    generationId = generationResult?.generationId,
    status = status,
    successCount = generationResult?.successCount ?: 0,
    failCount = generationResult?.failCount ?: 0,
)
