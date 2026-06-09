package com.examhelper.api.exam.application

import com.examhelper.api.exam.domain.Exam
import com.examhelper.api.exam.domain.entity.ExamItem
import com.examhelper.api.exam.domain.type.ExamStatus
import com.examhelper.api.exam.domain.vo.ExamGenerationResult
import com.examhelper.api.exam.port.inbound.GenerateExamUseCase
import com.examhelper.api.exam.port.inbound.command.GenerateExamCommand
import com.examhelper.api.exam.port.inbound.result.GenerateExamResult
import com.examhelper.api.exam.port.outbound.ExamStore
import com.examhelper.api.exam.port.outbound.GenerateExamQuestionsPort
import com.examhelper.api.exam.port.outbound.command.GenerateExamQuestionsCommand
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.ExamId
import com.examhelper.api.kernel.identifier.ExamItemId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GenerateExamService(
    private val examStore: ExamStore,
    private val generateExamQuestionsPort: GenerateExamQuestionsPort,
    private val idGenerator: IdGenerator,
) : GenerateExamUseCase {
    override fun execute(command: GenerateExamCommand): GenerateExamResult {
        val metadata = command.toMetadata()

        val exam = Exam.create(
            id = ExamId(idGenerator.generateId()),
            title = command.title,
            metadata = metadata,
        )

        examStore.save(exam)

        // 빈 모의고사 생성
        if (metadata.targetQuestionCount == 0) {
            exam.completeGeneration(
                ExamGenerationResult(
                    generationId = null,
                    successCount = 0,
                    failCount = 0,
                )
            )

            examStore.save(exam)

            return GenerateExamResult(
                examId = exam.id,
                generationId = null,
                status = exam.status,
                successCount = 0,
                failCount = 0,
            )
        }

        val generationResult = runCatching {
            generateExamQuestionsPort.generate(
                GenerateExamQuestionsCommand(
                    metadata,
                    command.frameSearchTopK
                )
            )
        }.getOrElse { ex ->
            exam.failGeneration(ex.message ?: "Unknown generation error")
            examStore.save(exam)

            return GenerateExamResult(
                examId = exam.id,
                generationId = null,
                status = ExamStatus.FAILED,
                successCount = 0,
                failCount = metadata.targetQuestionCount,
            )
        }

        generationResult.questionIds.forEachIndexed { index, questionId ->
            exam.addItem(
                ExamItem(
                    id = ExamItemId(idGenerator.generateId()),
                    questionId = questionId,
                    ordering = index + 1,
                )
            )
        }

        exam.completeGeneration(
            ExamGenerationResult(
                generationId = generationResult.generationId,
                successCount = generationResult.successCount,
                failCount = generationResult.failCount,
            )
        )

        examStore.save(exam)

        return GenerateExamResult(
            examId = exam.id,
            generationId = generationResult.generationId,
            status = exam.status,
            successCount = generationResult.successCount,
            failCount = generationResult.failCount,
        )
    }
}
