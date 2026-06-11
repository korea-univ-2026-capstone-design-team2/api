package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.question_generation.domain.vo.QuestionGenerationRequest
import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult
import org.springframework.stereotype.Component

@Component
class PromptAssembler {
    fun assembleUserPrompt(command: LlmGenerationCommand): String = buildString {
        appendGenerationRequest(command.generationRequest)

        appendLine()
        appendLine("==================================================")
        appendLine("[REFERENCE_FRAMES]")
        appendLine("==================================================")

        command.referenceFrames.forEachIndexed { index, frame -> appendReferenceFrame(index + 1, frame) }

        appendGenerationRules()
    }.trimEnd()

    private fun StringBuilder.appendGenerationRequest(req: QuestionGenerationRequest) {
        appendLine("[GENERATION_REQUEST]")

        appendLine("SUBJECT=${req.subject.name}")
        appendLine("QUESTION_TYPE=${req.questionType.name}")

        req.questionSubType?.let { appendLine("QUESTION_SUBTYPE=${it.name}") }

        appendLine("DIFFICULTY=${req.difficulty.name}")
        appendLine("TOPIC_CATEGORY=${req.topic.category}")

        req.topic.keyword?.let { appendLine("TOPIC_KEYWORD=$it") }

        req.topic.description?.let { appendLine("TOPIC_DESCRIPTION=$it") }
    }

    private fun StringBuilder.appendReferenceFrame(
        index: Int,
        frame: FrameSearchResult
    ) {
        appendLine()
        appendLine("FRAME_$index")

        appendLine("[QUESTION_METADATA]")
        appendLine("QUESTION_TYPE=${frame.questionType.name}")

        frame.questionSubType?.let { appendLine("QUESTION_SUBTYPE=${it.name}") }

        appendLine("DIFFICULTY=${frame.difficulty.name}")
        appendLine("TOPIC_CATEGORY=${frame.topicCategory}")

        frame.topicKeyword?.let { appendLine("TOPIC_KEYWORD=$it") }

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

    private fun StringBuilder.appendGenerationRules() {
        appendLine()
        appendLine("[GENERATION_RULES]")

        appendLine("- Preserve reasoning structure, trap patterns, and difficulty")
        appendLine("- Generate entirely new subject matter and entities")
        appendLine("- Do not reuse original wording from any reference frame")
        appendLine("- Maintain PSAT style logical rigor")
        appendLine("- Include realistic distractors")
        appendLine("- Ensure the correct answer is uniquely derivable")
        appendLine("- Preserve logical consistency between passage and choices")
        appendLine("- incorrectReasons must contain every incorrect choice")
        appendLine("- If answer=2 and choices are 1~5, incorrectReasons must contain keys 1,3,4,5")
        appendLine("- Never omit an incorrect choice explanation")
    }
}
