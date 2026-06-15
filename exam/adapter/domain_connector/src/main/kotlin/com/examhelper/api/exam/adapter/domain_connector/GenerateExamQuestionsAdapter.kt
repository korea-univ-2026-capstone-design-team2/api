package com.examhelper.api.exam.adapter.domain_connector

import com.examhelper.api.exam.domain.vo.ExamMetadata
import com.examhelper.api.exam.port.outbound.GenerateExamQuestionsPort
import com.examhelper.api.exam.port.outbound.command.GenerateExamQuestionsCommand
import com.examhelper.api.exam.port.outbound.result.GenerateExamQuestionsResult
import com.examhelper.api.kernel.identifier.MemberId
import com.examhelper.api.question_generation.port.inbound.GenerateQuestionUseCase
import com.examhelper.api.question_generation.port.inbound.command.GenerateQuestionCommand
import org.springframework.stereotype.Component

@Component
class GenerateExamQuestionsAdapter(
    private val generateQuestionUseCase: GenerateQuestionUseCase,
) : GenerateExamQuestionsPort {
    override fun generate(command: GenerateExamQuestionsCommand): GenerateExamQuestionsResult {
        val result = generateQuestionUseCase.execute(command.metadata.toCommand(
            memberId = command.memberId,
            frameSearchTopK = command.frameSearchTopK
        ))

        return GenerateExamQuestionsResult(
            generationId = result.questionGenerationId,
            questionIds = result.questionIds,
            successCount = result.successCount,
            failCount = result.failCount,
        )
    }

    private fun ExamMetadata.toCommand(memberId: MemberId, frameSearchTopK: Int): GenerateQuestionCommand =
        GenerateQuestionCommand(
            memberId = memberId,
            subject = subject,
            questionType = questionType,
            questionSubType = questionSubType,
            difficulty = difficulty,
            topicCategory = topic.category,
            topicKeyword = topic.keyword,
            topicDescription = topic.description,
            quantity = targetQuestionCount,
            frameSearchTopK = frameSearchTopK,
        )
}
