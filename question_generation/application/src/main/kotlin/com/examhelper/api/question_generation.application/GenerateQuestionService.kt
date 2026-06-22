package com.examhelper.api.question_generation.application

import com.examhelper.api.kernel.core.DomainEventPublisher
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionGenerationStepLogId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question_generation.application.factory.QuestionGenerationRequestFactory
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
    private val questionGenerationRequestFactory: QuestionGenerationRequestFactory,
    private val dispatcher: CoroutineDispatcher,
    private val applicationScope: CoroutineScope,
    private val domainEventPublisher: DomainEventPublisher,
) : GenerateQuestionUseCase {
    private val logger = KotlinLogging.logger {}

    override fun execute(command: GenerateQuestionCommand): GenerateQuestionResult {
        val generation = QuestionGeneration.create(
            id = QuestionGenerationId(idGenerator.generateId()),
            memberId = command.memberId,
            request = questionGenerationRequestFactory.create(command)
        )
        questionGenerationStore.save(generation)
        logger.info { "문제 생성 시작: generationId=${generation.id}, quantity=${generation.request.quantity}" }

        applicationScope.launch(dispatcher) { executeInternal(generation) }

        return GenerateQuestionResult(
            questionGenerationId = generation.id,
            questionIds = emptyList(),
            successCount = 0,
            failCount = 0,
            status = QuestionGenerationStatus.PENDING,
        )
    }

    // ── 파이프라인 오케스트레이터 ──────────────────────────────
    private suspend fun executeInternal(generation: QuestionGeneration) {
        val start = System.currentTimeMillis()
        try {
            val frames = searchFrames(generation) ?: return
            val results = generateQuestions(generation, frames)
            finalizeGeneration(generation, results)
        } catch (e: Exception) {
            logger.error(e) { "문제 생성 중 예외 발생: generationId=${generation.id}" }
            failAndPublish(generation, "예기치 않은 오류: ${e.message}")
        } finally {
            metricsPort.recordTotalDuration(System.currentTimeMillis() - start)
        }
    }

    // ── 프레임 탐색 ────────────────────────────────────────────

    private suspend fun searchFrames(generation: QuestionGeneration): List<FrameSearchResult>? {
        val frames = runWithLog(generation.id, QuestionGenerationStep.FRAME_SEARCH) {
            frameSearchPort.search(FrameSearchQuery.from(generation.request))
        }.getOrElse {
            logger.error(it) { "프레임 탐색 실패: generationId=${generation.id}" }
            failAndPublish(generation, "프레임 탐색 실패: generationId=${generation.id}")
            return null
        }

        if (frames.isEmpty()) {
            logger.warn { "프레임 검색 결과가 없습니다: generationId=${generation.id}, category=${generation.request.topic.category}" }
            failAndPublish(generation, "프레임을 찾지 못했습니다: ${generation.request.topic.category}")
            return null
        }

        return frames
    }

    // ── 병렬 문제 생성 ─────────────────────────────────────────
    private suspend fun generateQuestions(
        generation: QuestionGeneration,
        frames: List<FrameSearchResult>
    ): List<Result<QuestionId>> {
        val referenceFrames = assignFrames(frames, generation.request.quantity)

        return coroutineScope {
            referenceFrames.mapIndexed { index, referenceFrame ->
                async {
                    generateSingleQuestion(
                        generation = generation,
                        frames = frames,
                        referenceFrame = referenceFrame,
                        index = index
                    )
                }
            }.awaitAll()
        }
    }

    // ── 단일 문제 생성 ─────────────────────────────────────────
    private suspend fun generateSingleQuestion(
        generation: QuestionGeneration,
        frames: List<FrameSearchResult>,
        referenceFrame: FrameSearchResult,
        index: Int
    ): Result<QuestionId> {
        val llmResult = runWithLog(
            generationId = generation.id,
            step = QuestionGenerationStep.LLM_CALL,
            detail = "index=$index"
        ) {
            llmGenerationPort.generate(
                LlmGenerationCommand(
                    generationRequest = generation.request,
                    referenceFrames = frames
                )
            )
        }
            .onFailure { logger.error(it) { "LLM 생성 실패: generationId=${generation.id}, index=$index" } }
            .getOrElse { return Result.failure(it) }

        val questionId = runWithLog(generation.id, QuestionGenerationStep.QUESTION_CREATION, "index=$index") {
            questionCreationPort.create(
                QuestionCreationCommand(
                    result = llmResult,
                    memberId = generation.memberId,
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
        }
            .onFailure { logger.error(it) { "문제 생성 실패: generationId=${generation.id}, index=$index" } }
            .getOrElse { return Result.failure(it) }

        generation.markQuestionGenerated(
            questionId = questionId,
            ordering = index + 1
        )

        domainEventPublisher.publishFrom(generation)

        return Result.success(questionId)
    }

    // ── 완료 처리 ──────────────────────────────────────────────
    private suspend fun finalizeGeneration(
        generation: QuestionGeneration,
        results: List<Result<QuestionId>>
    ) {
        val successIds = results.mapNotNull { it.getOrNull() }
        val failureCount = results.count { it.isFailure }

        logger.info { "문제 생성 완료: total=${generation.request.quantity}, failed=$failureCount" }

        generation.complete(
            successCount = successIds.size,
            failureCount = failureCount,
        )

        questionGenerationStore.save(generation)

        domainEventPublisher.publishFrom(generation)
    }

    // ── 실패 처리 ──────────────────────────────────────────────
    private suspend fun failAndPublish(generation: QuestionGeneration, message: String) {
        generation.fail(message)
        questionGenerationStore.save(generation)
        domainEventPublisher.publishFrom(generation)
    }

    // ── 스텝 로깅 ──────────────────────────────────────────────
    private suspend fun <T> runWithLog(
        generationId: QuestionGenerationId,
        step: QuestionGenerationStep,
        detail: String? = null,
        block: suspend () -> T,
    ): Result<T> {
        val start = System.currentTimeMillis()
        return runCatching { block() }.also {
            stepLogStore.save(
                QuestionGenerationStepLog(
                    id = QuestionGenerationStepLogId(idGenerator.generateId()),
                    generationId = generationId,
                    step = step,
                    status = if (it.isSuccess) QuestionGenerationStepStatus.SUCCESS
                    else QuestionGenerationStepStatus.FAILED,
                    durationMs = System.currentTimeMillis() - start,
                    detail = it.exceptionOrNull()?.message ?: detail,
                    occurredAt = Instant.now(),
                )
            )
        }
    }

    private fun assignFrames(
        frames: List<FrameSearchResult>,
        quantity: Int
    ): List<FrameSearchResult> {
        if (frames.size >= quantity) { return frames.shuffled().take(quantity) }
        val shuffled = frames.shuffled()

        return (0 until quantity).map { shuffled[it % shuffled.size] }
    }
}
