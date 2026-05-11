package com.examhelper.api.question_generation.application

import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionGenerationStepLogId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question_generation.domain.QuestionGeneration
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStatus
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStep
import com.examhelper.api.question_generation.domain.type.QuestionGenerationStepStatus
import com.examhelper.api.question_generation.port.inbound.GenerateQuestionUseCase
import com.examhelper.api.question_generation.port.inbound.command.GenerateQuestionCommand
import com.examhelper.api.question_generation.port.inbound.model.QuestionGenerationStepLog
import com.examhelper.api.question_generation.port.inbound.result.GenerateQuestionResult
import com.examhelper.api.question_generation.port.outbound.FrameSearchPort
import com.examhelper.api.question_generation.port.outbound.LlmGenerationPort
import com.examhelper.api.question_generation.port.outbound.QuestionCreationPort
import com.examhelper.api.question_generation.port.outbound.QuestionGenerationStepLogStore
import com.examhelper.api.question_generation.port.outbound.QuestionGenerationStore
import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationCommand
import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationMetadata
import com.examhelper.api.question_generation.port.outbound.query.FrameSearchQuery
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GenerateQuestionService(
    private val questionGenerationStore: QuestionGenerationStore,
    private val stepLogStore: QuestionGenerationStepLogStore,
    private val frameSearchPort: FrameSearchPort,
    private val llmGenerationPort: LlmGenerationPort,
    private val questionCreationPort: QuestionCreationPort,
    private val idGenerator: IdGenerator
) : GenerateQuestionUseCase {
    private val logger = KotlinLogging.logger {}

    override fun execute(command: GenerateQuestionCommand): GenerateQuestionResult {
        val generation = QuestionGeneration.create(
            id = QuestionGenerationId(idGenerator.generateId()),
            request = command.toGenerationRequest(),
        )
        questionGenerationStore.save(generation)

        // ── FrameSearch ────────────────────────────
        val frames = runWithLog(generation.id, QuestionGenerationStep.FRAME_SEARCH) {
            frameSearchPort.search(FrameSearchQuery.from(generation.request))
        }.getOrElse {
            generation.fail("Frame search failed: ${it.message}")
            questionGenerationStore.save(generation)

            return GenerateQuestionResult(
                questionGenerationId = generation.id,
                questionIds = emptyList(),
                successCount = 0,
                failCount = generation.request.quantity,
                status = QuestionGenerationStatus.FAILED
            )
        }

        if (frames.isEmpty()) {
            generation.fail("프레임을 찾지 못했습니다.: ${generation.request.topic.category}")
            questionGenerationStore.save(generation)
            return GenerateQuestionResult(
                questionGenerationId = generation.id,
                questionIds = emptyList(),
                successCount = 0,
                failCount = generation.request.quantity,
                status = QuestionGenerationStatus.FAILED
            )
        }

        // ── LlmGeneration ────────────────────────────
        val createdQuestionIds = mutableListOf<QuestionId>()
        val failures = mutableListOf<String>()

        repeat(generation.request.quantity) {
            val llmResult = runWithLog<LlmGenerationResult>(
                generationId = generation.id,
                step = QuestionGenerationStep.LLM_CALL,
                detail = "index=$it",
            ) {
                llmGenerationPort.generate(LlmGenerationCommand(generation.request, frames))
            }.getOrElse {
                val message = "Llm 생성과정 실패: index=$it, reason=${it.message}"
                failures += message;
                return@repeat
            }

            val referenceFrame = frames.random()
            logger.info("LLM generation succeeded: index=$it, stem=${llmResult.stem.take(30)}...")

            // ── CreateQuestion ────────────────────────────
            runWithLog(
                generationId = generation.id,
                step = QuestionGenerationStep.QUESTION_CREATION,
                detail = "index=$it",
            ) {
                questionCreationPort.create(
                    QuestionCreationCommand(
                        result = llmResult,
                        generationId = generation.id,
                        metadata = QuestionCreationMetadata(
                            subject = generation.request.subject,
                            questionType = generation.request.questionType,
                            questionSubType = generation.request.questionSubType,
                            difficulty = generation.request.difficulty,
                            topicCategory = generation.request.topic.category,
                            topicKeyword = generation.request.topic.keyword,
                            frameId = referenceFrame.frameId,
                            similarityScore = referenceFrame.similarityScore
                        )
                    )
                )
            }.onSuccess {result ->
                createdQuestionIds += result.questionId
            }.onFailure {e ->
                val message = "문제 생성 실패: index=$e, reason=${e.message}"
                failures += message
                logger.error("문제 생성 실패: index=$e", e)
                e.printStackTrace()
            }
        }

        logger.info("Generation completed: total=${generation.request.quantity}, failed=${failures.size}")

        if (failures.isNotEmpty()) {
            generation.fail("${failures.size}/${generation.request.quantity} questions failed")
        } else {
            generation.complete()
        }

        // ─────────────────────────────────────────────
        // Finalize
        // ─────────────────────────────────────────────
        val successCount = createdQuestionIds.size
        val failCount = failures.size

        val status = when {
            successCount == 0 -> QuestionGenerationStatus.FAILED
            else -> QuestionGenerationStatus.COMPLETED
        }

        questionGenerationStore.save(generation)

        return GenerateQuestionResult(
            questionGenerationId = generation.id,
            questionIds = createdQuestionIds,
            successCount = successCount,
            failCount = failCount,
            status = status
        )
    }

    private fun <T> runWithLog(
        generationId: QuestionGenerationId,
        step: QuestionGenerationStep,
        detail: String? = null,
        block: () -> T,
    ): Result<T> {
        val start = System.currentTimeMillis()
        return runCatching(block).also { result ->
            stepLogStore.save(
                QuestionGenerationStepLog(
                    id = QuestionGenerationStepLogId(idGenerator.generateId()),
                    generationId = generationId,
                    step = step,
                    status = if (result.isSuccess) QuestionGenerationStepStatus.SUCCESS else QuestionGenerationStepStatus.FAILED,
                    durationMs = System.currentTimeMillis() - start,
                    detail = result.exceptionOrNull()?.message ?: detail,
                    occurredAt = Instant.now()
                )
            )
        }
    }
}
