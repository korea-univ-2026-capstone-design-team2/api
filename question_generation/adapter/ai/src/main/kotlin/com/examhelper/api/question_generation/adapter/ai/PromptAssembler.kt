package com.examhelper.api.question_generation.adapter.ai

import com.examhelper.api.question_generation.port.outbound.command.LlmGenerationCommand
import org.springframework.stereotype.Component

@Component
class PromptAssembler {
    fun assembleUserPrompt(command: LlmGenerationCommand): String = buildString {
        val req = command.generationRequest

        appendLine("[생성 조건]")
        appendLine("- 과목: ${req.subject.name}")
        appendLine("- 문제 유형: ${req.questionType.name}")
        req.questionSubType?.let { appendLine("- 하위 유형: ${it.name}") }
        appendLine("- 난이도: ${req.difficulty.name}")
        appendLine("- 주제 카테고리: ${req.topic.category}")
        req.topic.keyword?.let { appendLine("- 주제 키워드: $it") }
        req.topic.description?.let { appendLine("- 주제 설명: $it") }
        appendLine()
        appendLine("[참조 프레임]")
        appendLine("아래 기출 문제의 논리 구조를 참고하여 동일한 패턴으로 출제하십시오.")
        appendLine()

        command.referenceFrames.forEachIndexed { index, frame ->
            appendLine("── 참조 ${index + 1} (유사도: ${"%.2f".format(frame.similarityScore)}) ──")
            appendLine("논리 구조: ${frame.logicalStructureSummary}")
            frame.abstractPassage?.let { appendLine("지문 구조: $it") }
            frame.argumentPattern?.let { appendLine("논증 패턴: $it") }
            appendLine("질문 템플릿: ${frame.stemTemplate}")
            appendLine("선지 패턴: ${frame.choicePattern}")
            appendLine()
            appendLine("[원본 예시]")
            appendLine("질문: ${frame.originalStem}")
            frame.originalPassage?.let { appendLine("지문: $it") }
            appendLine("선지:")
            frame.originalChoices.forEachIndexed { i, choice ->
                appendLine("  ${i + 1}. $choice")
            }
            appendLine("해설: ${frame.originalExplanation}")
            appendLine()
        }
    }.trimEnd()
}
