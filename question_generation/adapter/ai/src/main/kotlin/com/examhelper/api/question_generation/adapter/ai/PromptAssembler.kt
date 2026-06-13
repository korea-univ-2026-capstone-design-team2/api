package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class PromptAssembler {
    private val logger = KotlinLogging.logger {}

    fun assembleUserPrompt(command: LlmGenerationCommand): String = buildString {
        appendGenerationRequest(command.generationRequest)

        appendLine()
        appendLine("==================================================")
        appendLine("[REFERENCE_FRAMES]")
        appendLine("==================================================")

        command.referenceFrames.forEachIndexed { index, frame ->
            appendReferenceFrame(index + 1, frame)
        }

        appendGenerationRules()
    }.trimEnd()

    private fun StringBuilder.appendGenerationRequest(req: QuestionGenerationRequest) {
        appendLine("[GENERATION_REQUEST]")
        appendLine("SUBJECT=${req.subject.name}")
        appendLine("QUESTION_TYPE=${req.questionType.name}")
        req.questionSubType?.let { appendLine("QUESTION_SUBTYPE=${it.name}") }
        appendLine("DIFFICULTY=${req.difficulty.name}")
        appendLine("TOPIC_CATEGORY=${req.topic.category}")
        req.topic.keyword?.let { appendLine("TOPIC_KEYWORD=${it}") }
        req.topic.description?.let { appendLine("TOPIC_DESCRIPTION=${it}") }
    }

    private fun StringBuilder.appendReferenceFrame(index: Int, frame: FrameSearchResult) {
        appendLine()
        appendLine("FRAME_$index")

        appendLine("[QUESTION_METADATA]")
        appendLine("QUESTION_TYPE=${frame.questionType.name}")
        frame.questionSubType?.let { appendLine("QUESTION_SUBTYPE=${it.name}") }
        appendLine("DIFFICULTY=${frame.difficulty.name}")
        appendLine("TOPIC_CATEGORY=${frame.topicCategory}")
        frame.topicKeyword?.let { appendLine("TOPIC_KEYWORD=${it}") }

        appendLine("[ORIGINAL PASSAGE] (FOR STRUCTURAL & TONE REFERENCE ONLY)")
        appendLine(frame.passage)
        appendLine("---------------------------------------------------------------")

        val passageLength = frame.passage?.length
        appendPassageLengthReference(passageLength)

        appendLine()
        appendLine("[REASONING_FRAME]")
        appendLine("REASONING_TYPE=${frame.reasoningType}")

        appendLine("PREMISES")
        frame.premises.forEach { appendLine("- $it") }

        appendLine("CONDITIONS")
        frame.conditions.forEach { appendLine("- $it") }

        appendLine("LOGICAL_GOAL")
        appendLine(frame.logicalGoal)

        appendLine("INFERENCE_STRUCTURE")
        frame.inferenceStructure.forEach { appendLine("- $it") }

        appendLine()
        appendLine("[REASONING_PATTERNS]")
        frame.reasoningPatterns.forEach { appendLine("- $it") }

        appendLine()
        appendLine("[TRAP_PATTERNS]")
        frame.trapPatterns.forEach { appendLine("- $it") }

        appendLine()
        appendLine("[DISCOURSE_STRUCTURE]")
        frame.discourseStructure.forEach { appendLine("- $it") }

        appendLine()
        appendLine("[COGNITIVE_OPERATIONS]")
        frame.cognitiveOperations.forEach { appendLine("- $it") }

        appendLine()
        appendLine("[REASONING_COMPLEXITY]")
        appendLine(frame.reasoningComplexity)

        appendLine()
        appendLine("[GENERATION_CONSTRAINTS]")

        appendLine("MUST_PRESERVE")
        frame.mustPreserve.forEach { appendLine("- $it") }

        appendLine("VARIABLE_ELEMENTS")
        frame.variableElements.forEach { appendLine("- $it") }

        appendLine()
        appendLine("----------------------------------------")
    }

    private fun StringBuilder.appendPassageLengthReference(originalPassageLength: Int?) {
        appendLine("[PASSAGE_LENGTH_REFERENCE]")

        if (originalPassageLength == null) {
            appendLine("NO_PASSAGE")
            appendLine("- This question type has no shared passage.")
            appendLine("- sharedContext must be null.")
            return
        }

        val range = calculatePassageLengthRange(originalPassageLength)

        appendLine("ORIGINAL_PASSAGE_LENGTH=$originalPassageLength")
        appendLine("REQUIRED_RANGE=${range.first}~${range.last} characters")

        appendLine("- sharedContext.content must be non-null.")
        appendLine("- Generate a passage approximately the same length as the reference passage.")
        appendLine("- The passage should feel similar in overall volume and reading time.")
        appendLine("- Do not generate a noticeably shorter summary-style passage.")
        appendLine("- Do not generate a significantly longer expanded passage.")
        appendLine("- Character count should remain within REQUIRED_RANGE.")
    }

    private fun calculatePassageLengthRange(passageLength: Int): IntRange {
        val min = (passageLength * 0.9 * 1.45).toInt()
        val max = (passageLength * 1.1 * 1.45).toInt()

        logger.info { "min: $min..max: $max" }

        return min..max
    }

    private fun StringBuilder.appendGenerationRules() {
        appendLine()
        appendLine("[GENERATION_RULES]")
        appendLine("- Preserve reasoning structure, trap patterns, and difficulty from the reference frame.")
        appendLine("- Generate entirely new subject matter and entities.")
        appendLine("- Never reuse original wording, vocabulary, or proper nouns from any reference frame.")
        appendLine("- Maintain PSAT-style logical rigor throughout.")
        appendLine("- Include realistic and attractive distractors.")
        appendLine("- Ensure the correct answer is uniquely derivable from sharedContext.")
        appendLine("- Maintain logical consistency between sharedContext and all choices.")
        appendLine("- incorrectReasons must contain keys 1 through 5.")
        appendLine("- For the correct choice key in incorrectReasons, write the same content as correctReason.")
        appendLine("- Never omit an explanation for any choice.")
    }
}
