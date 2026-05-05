package com.examhelper.api.question_generation.application

import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionGenerationStepLogId
import com.examhelper.api.question_generation.domain.QuestionGeneration
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
): GenerateQuestionUseCase {
    private val logger = KotlinLogging.logger {}

    override fun execute(command: GenerateQuestionCommand): GenerateQuestionResult {
        val generation = QuestionGeneration.create(
            id      = QuestionGenerationId(idGenerator.generateId()),
            request = command.toGenerationRequest(),
        )
        questionGenerationStore.save(generation)   // INSERT 1회

        // ── FrameSearch ────────────────────────────
        val frames = runWithLog(generation.id, QuestionGenerationStep.FRAME_SEARCH) {
            //frameSearchPort.search(FrameSearchQuery.from(generation.request))
            listOf(FrameSearchResult(
                frameId = "dummyFrameId",
                similarityScore = 0.9,
                questionType = generation.request.questionType,
                questionSubType = generation.request.questionSubType,
                difficulty = generation.request.difficulty,
                abstractPassage = "abstractPassage",
                logicalStructureSummary = "logicalStructureSummary",
                argumentPattern = "argumentPattern",
                stemTemplate = "stemTemplate",
                choicePattern = "choicePattern",
                originalStem = "originalStem",
                originalPassage = "originalPassage",
                originalChoices = listOf("choice1", "choice2", "choice3", "choice4", "choice5"),
                originalExplanation = "originalExplanation"
            ))
        }.getOrElse { ex ->
            generation.fail("Frame search failed: ${ex.message}")
            questionGenerationStore.save(generation)

            return GenerateQuestionResult(generation.id)
        }

        if (frames.isEmpty()) {
            generation.fail("No frames found: ${generation.request.topic.category}")
            questionGenerationStore.save(generation)
            return GenerateQuestionResult(generation.id)
        }

        var failCount = 0

        // ── LlmGeneration ────────────────────────────
        repeat(generation.request.quantity) { index ->
            val llmResult = runWithLog<LlmGenerationResult>(
                generationId = generation.id,
                step         = QuestionGenerationStep.LLM_CALL,
                detail       = "index=$index",
            ) {
                llmGenerationPort.generate(LlmGenerationCommand(generation.request, frames))
            }.getOrElse { failCount++; return@repeat }

            //val referenceFrame = frames.first()
            logger.info("LLM generation succeeded: index=$index, stem=${llmResult.stem.take(30)}...")

            // ── CreateQuestion ────────────────────────────
            runWithLog(
                generationId = generation.id,
                step         = QuestionGenerationStep.QUESTION_CREATION,
                detail       = "index=$index",
            ) {
                questionCreationPort.create(QuestionCreationCommand(
                    result = llmResult,
                    generationId = generation.id,
                    metadata = QuestionCreationMetadata(
                        subject = generation.request.subject,
                        questionType = generation.request.questionType,
                        questionSubType = generation.request.questionSubType,
                        difficulty = generation.request.difficulty,
                        topicCategory = generation.request.topic.category,
                        topicKeyword = generation.request.topic.keyword,
                        frameId = "referenceFrame.frameId",
                        similarityScore = 0.0
                    )
                ))
            }.onFailure { e ->
                failCount++
                logger.error("Question creation failed: index=$index", e)
                e.printStackTrace()
            }
        }

        logger.info("Generation completed: total=${generation.request.quantity}, failed=$failCount")

        if (failCount > 0) {
            generation.fail("$failCount/${generation.request.quantity} questions failed")
        } else {
            generation.complete()
        }

        questionGenerationStore.save(generation)

        return GenerateQuestionResult(generation.id)

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
                    step         = step,
                    status       = if (result.isSuccess) QuestionGenerationStepStatus.SUCCESS else QuestionGenerationStepStatus.FAILED,
                    durationMs   = System.currentTimeMillis() - start,
                    detail       = result.exceptionOrNull()?.message ?: detail,
                    occurredAt   = Instant.now()
                )
            )
        }
    }
}
