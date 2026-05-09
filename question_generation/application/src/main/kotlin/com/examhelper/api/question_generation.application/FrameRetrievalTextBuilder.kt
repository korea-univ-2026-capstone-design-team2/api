package com.examhelper.api.question_generation.application

import com.examhelper.api.question_generation.port.inbound.model.LogicalFrameDocument
import org.springframework.stereotype.Component

@Component
class FrameRetrievalTextBuilder {
    fun build(frame: LogicalFrameDocument): String {
        return buildString {
            appendLine("[QUESTION_TYPE]")
            appendLine(frame.questionType)

            frame.questionSubType?.let {
                appendLine()
                appendLine("[QUESTION_SUBTYPE]")
                appendLine(it)
            }
            appendLine()

            appendLine("[DIFFICULTY]")
            appendLine(frame.difficulty)
            appendLine()

            appendLine("[REASONING_TYPE]")
            appendLine(frame.reasoningType)
            appendLine()

            appendLine("[TOPIC_CATEGORY]")
            appendLine(frame.topicCategory)
            frame.topicKeyword?.let {
                appendLine()
                appendLine("[TOPIC_KEYWORD]")
                appendLine(it)
            }
            appendLine()

            appendLine("[GOAL]")
            appendLine(frame.logicalGoal)
            appendLine()

            appendLine("[PREMISES]")
            frame.premises.forEach { appendLine(it) }
            appendLine()

            appendLine("[CONDITIONS]")
            frame.conditions.forEach { appendLine(it) }
            appendLine()

            appendLine("[INFERENCE_STRUCTURE]")
            frame.inferenceStructure.forEach { appendLine(it) }
            appendLine()

            appendLine("[REASONING_PATTERNS]")
            frame.reasoningPatterns.forEach { appendLine(it) }
            appendLine()

            appendLine("[TRAP_PATTERNS]")
            frame.trapPatterns.forEach { appendLine(it) }
            appendLine()

            appendLine("[DISCOURSE_STRUCTURE]")
            frame.discourseStructure.forEach { appendLine(it) }
            appendLine()

            appendLine("[COGNITIVE_OPERATIONS]")
            frame.cognitiveOperations.forEach { appendLine(it) }
            appendLine()

            appendLine("[REASONING_COMPLEXITY]")
            appendLine(frame.reasoningComplexity)
            appendLine()

            appendLine("[MUST_PRESERVE]")
            frame.mustPreserve.forEach { appendLine(it) }
            appendLine()

            appendLine("[VARIABLE_ELEMENTS]")
            frame.variableElements.forEach { appendLine(it) }
        }
    }
}
