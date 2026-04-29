package com.examhelper.api.question.application

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.PublishQuestionUseCase
import com.examhelper.api.question.port.inbound.command.PublishQuestionCommand
import com.examhelper.api.question.port.inbound.result.PublishQuestionResult
import com.examhelper.api.question.port.outbound.QuestionStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PublishQuestionService(
    private val questionStore: QuestionStore,
) : PublishQuestionUseCase {

    @Transactional
    override fun execute(command: PublishQuestionCommand): PublishQuestionResult {
        val question = questionStore.loadById(QuestionId(command.questionId))
            ?: throw QuestionException.NotFound(command.questionId.toString())

        question.publish()
        questionStore.save(question)

        return PublishQuestionResult(
            questionId = question.id.value,
            status = question.status,
        )
    }
}
