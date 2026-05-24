package com.examhelper.api.question.application

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

        // 1. Exam Aggregate 생성 — GENERATING 상태로 즉시 시작
        val exam = Exam.create(
            id = ExamId(idGenerator.generateId()),
            title = command.title,
            metadata = metadata,
        )
        examStore.save(exam)

        // 2. Question 생성 위임 (AI RAG 호출)
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

        // 3. 생성된 QuestionItem을 ExamItem으로 변환하여 편입
        generationResult.questionIds.forEachIndexed { index, questionId ->
            exam.addItem(
                ExamItem(
                    id = ExamItemId(idGenerator.generateId()),
                    questionId = questionId,
                    ordering = index + 1
                )
            )
        }

        // 4. 생성 완료 처리 — READY 전이 + 이벤트 발행
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
