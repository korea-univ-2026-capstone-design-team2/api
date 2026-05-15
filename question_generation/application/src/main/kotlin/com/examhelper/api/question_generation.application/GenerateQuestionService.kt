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
import com.examhelper.api.question_generation.port.outbound.QuestionGenerationMetricsPort
import com.examhelper.api.question_generation.port.outbound.QuestionGenerationStepLogStore
import com.examhelper.api.question_generation.port.outbound.QuestionGenerationStore
import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationCommand
import com.examhelper.api.question_generation.port.outbound.command.QuestionCreationMetadata
import com.examhelper.api.question_generation.port.outbound.query.FrameSearchQuery
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult
import com.examhelper.api.question_generation.port.outbound.result.LlmGenerationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
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
    private val idGenerator: IdGenerator,
    private val metricsPort: QuestionGenerationMetricsPort,
    private val dispatcher: CoroutineDispatcher
) : GenerateQuestionUseCase {
    private val logger = KotlinLogging.logger {}

    override fun execute(command: GenerateQuestionCommand): GenerateQuestionResult =
        runBlocking(dispatcher) {
            executeInternal(command)
        }

    private suspend fun executeInternal(command: GenerateQuestionCommand): GenerateQuestionResult {
        val start = System.currentTimeMillis()

        try {
            val generation = QuestionGeneration.create(
                id = QuestionGenerationId(idGenerator.generateId()),
                request = command.toGenerationRequest(),
            )
            questionGenerationStore.save(generation)

            logger.info { "문제 생성 시작: generationId=${generation.id}, quantity=${generation.request.quantity}" }

            // ── FrameSearch ────────────────────────────────────────
            val frames = runWithLog(generation.id, QuestionGenerationStep.FRAME_SEARCH) {
                frameSearchPort.search(FrameSearchQuery.from(generation.request))
            }.getOrElse {
                val message = "프레임 탐색 실패: generationId=${generation.id}"
                logger.error(it) { message }
                generation.fail(message)
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
                logger.warn { "프레임 검색 결과가 없습니다.: generationId=${generation.id}, category=${generation.request.topic.category}" }
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

            // ── 병렬 문제 생성 ─────────────────────────────────────
            val framePerSlot = List(generation.request.quantity) { frames[it % frames.size] }

            val results: List<Result<QuestionId>> = coroutineScope {
                (0 until generation.request.quantity).map { index ->
                    async {
                        generateSingleQuestion(
                            generation = generation,
                            frames = frames,
                            referenceFrame = framePerSlot[index],
                            index = index
                        )
                    }
                }.awaitAll()
            }

            // ── Finalize ───────────────────────────────────────────
            val createdQuestionIds = results.mapNotNull { it.getOrNull() }
            val failures = results.filter { it.isFailure }

            logger.info { "문제 생성 완료: total=${generation.request.quantity}, failed=${failures.size}" }

            if (failures.isNotEmpty()) {
                generation.fail("${failures.size}/${generation.request.quantity} 문제 생성 실패")
            } else {
                generation.complete()
            }

            questionGenerationStore.save(generation)

            return GenerateQuestionResult(
                questionGenerationId = generation.id,
                questionIds = createdQuestionIds,
                successCount = createdQuestionIds.size,
                failCount = failures.size,
                status = generation.status
            )
        } finally {
            metricsPort.recordTotalDuration(System.currentTimeMillis() - start)
        }
    }

    // ── 단일 문제 생성 파이프라인 ──────────────────────────────────
    private suspend fun generateSingleQuestion(
        generation: QuestionGeneration,
        frames: List<FrameSearchResult>,
        referenceFrame: FrameSearchResult,
        index: Int,
    ): Result<QuestionId> {
        val llmResult = runWithLog(
            generationId = generation.id,
            step = QuestionGenerationStep.LLM_CALL,
            detail = "index=$index"
        ) {
            llmGenerationPort.generate(LlmGenerationCommand(generation.request, frames))
        }.getOrElse {
            logger.error(it) { "Llm 생성과정 실패: generationId=${generation.id}, index=$index" }
            return Result.failure(it)
        }

        return runWithLog(
            generationId = generation.id,
            step = QuestionGenerationStep.QUESTION_CREATION,
            detail = "index=$index",
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
            ).questionId
        }.onFailure {
            logger.error(it) { "문제 생성 실패: generationId=${generation.id}, index=$index" }
        }
    }

    // ── suspend 버전 runWithLog ────────────────────────────────
    private suspend fun <T> runWithLog(
        generationId: QuestionGenerationId,
        step: QuestionGenerationStep,
        detail: String? = null,
        block: suspend () -> T,
    ): Result<T> {
        val start = System.currentTimeMillis()
        return runCatching { block() }.also { result ->
            stepLogStore.save(
                QuestionGenerationStepLog(
                    id = QuestionGenerationStepLogId(idGenerator.generateId()),
                    generationId = generationId,
                    step = step,
                    status = if (result.isSuccess) QuestionGenerationStepStatus.SUCCESS
                    else QuestionGenerationStepStatus.FAILED,
                    durationMs = System.currentTimeMillis() - start,
                    detail = result.exceptionOrNull()?.message ?: detail,
                    occurredAt = Instant.now()
                )
            )
        }
    }
}
