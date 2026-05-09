package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import org.springframework.stereotype.Component

@Component
class PromptAssembler {
    fun assembleUserPrompt(command: LlmGenerationCommand): String = buildString {
        val req = command.generationRequest

        appendLine("[GENERATION_REQUEST]")
        appendLine("SUBJECT:")
        appendLine(req.subject.name)
        appendLine()

        appendLine("QUESTION_TYPE:")
        appendLine(req.questionType.name)
        req.questionSubType?.let {
            appendLine()
            appendLine("QUESTION_SUBTYPE:")
            appendLine(it.name)
        }
        appendLine()

        appendLine("DIFFICULTY:")
        appendLine(req.difficulty.name)
        appendLine()

        appendLine("TOPIC_CATEGORY:")
        appendLine(req.topic.category)
        req.topic.keyword?.let {
            appendLine()
            appendLine("TOPIC_KEYWORD:")
            appendLine(it)
        }
        req.topic.description?.let {
            appendLine()
            appendLine("TOPIC_DESCRIPTION:")
            appendLine(it)
        }
        appendLine()

        appendLine("==================================================")
        appendLine("[REFERENCE_FRAMES]")
        appendLine("==================================================")
        appendLine()

        command.referenceFrames.forEachIndexed { index, frame ->
            appendLine("##############################")
            appendLine("REFERENCE_FRAME_${index + 1}")
            appendLine("##############################")
            appendLine()

            appendLine("[SIMILARITY_SCORE]")
            appendLine("%.4f".format(frame.similarityScore))
            appendLine()

            appendLine("[QUESTION_METADATA]")
            appendLine("QUESTION_TYPE:")
            appendLine(frame.questionType.name)
            frame.questionSubType?.let {
                appendLine()
                appendLine("QUESTION_SUBTYPE:")
                appendLine(it.name)
            }
            appendLine()

            appendLine("DIFFICULTY:")
            appendLine(frame.difficulty.name)
            appendLine()

            appendLine("TOPIC_CATEGORY:")
            appendLine(frame.topicCategory)
            frame.topicKeyword?.let {
                appendLine()
                appendLine("TOPIC_KEYWORD:")
                appendLine(it)
            }
            appendLine()

            appendLine("[REASONING_FRAME]")
            appendLine("REASONING_TYPE:")
            appendLine(frame.reasoningType)
            appendLine()

            appendLine("PREMISES:")
            frame.premises.forEach { appendLine("- $it") }
            appendLine()

            appendLine("CONDITIONS:")
            frame.conditions.forEach { appendLine("- $it") }
            appendLine()

            appendLine("LOGICAL_GOAL:")
            appendLine(frame.logicalGoal)
            appendLine()

            appendLine("INFERENCE_STRUCTURE:")
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
            appendLine("MUST_PRESERVE:")
            frame.mustPreserve.forEach { appendLine("- $it") }
            appendLine()

            appendLine("VARIABLE_ELEMENTS:")
            frame.variableElements.forEach { appendLine("- $it") }
            appendLine()

            appendLine("[ORIGINAL_PROBLEM]")
            appendLine("STEM:")
            appendLine(frame.questionStem)
            frame.passage?.let {
                appendLine()
                appendLine("PASSAGE:")
                appendLine(it)
            }
            frame.passageDescription?.let {
                appendLine()
                appendLine("PASSAGE_DESCRIPTION:")
                appendLine(it)
            }
            appendLine()

            appendLine("CHOICES:")
            frame.answerChoices.forEachIndexed { i, choice -> appendLine("${i + 1}. $choice") }
            appendLine()

            appendLine("ANSWER:")
            appendLine(frame.correctAnswer)
            appendLine()

            appendLine("CORRECT_REASON:")
            appendLine(frame.correctReason)
            appendLine()

            appendLine("[RETRIEVAL_TEXT]")
            appendLine(frame.retrievalText)
            appendLine()

            appendLine("==================================================")
            appendLine()
        }

        appendLine("[GENERATION_RULES]")
        appendLine("- 참조 프레임의 논리 구조와 추론 패턴을 유지하되 내용은 새롭게 생성할 것")
        appendLine("- 동일 표현 반복 금지")
        appendLine("- trap pattern 을 반드시 포함할 것")
        appendLine("- reasoning complexity 수준 유지")
        appendLine("- 선택지는 실제 PSAT 스타일처럼 구성할 것")
        appendLine("- 정답은 명확한 reasoning chain 으로 도출 가능해야 함")
        appendLine("- passage 와 choice 사이의 논리적 정합성을 유지할 것")
        appendLine("- 한국 PSAT/5급 공채 언어논리 스타일 유지")
    }.trimEnd()
}
