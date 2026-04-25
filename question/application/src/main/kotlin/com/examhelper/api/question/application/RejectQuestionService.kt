package com.examhelper.api.question.application

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.RejectQuestionUseCase
import com.examhelper.api.question.port.inbound.command.RejectQuestionCommand
import com.examhelper.api.question.port.inbound.result.RejectQuestionResult
import com.examhelper.api.question.port.outbound.QuestionStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RejectQuestionService(
    private val questionStore: QuestionStore,
) : RejectQuestionUseCase {
    @Transactional
    override fun execute(command: RejectQuestionCommand): RejectQuestionResult {
        val question = questionStore.loadById(QuestionId(command.questionId))
            ?: throw QuestionException.NotFound(command.questionId.toString())

        question.reject()
        questionStore.save(question)

        return RejectQuestionResult(
            questionId = question.id.value,
            status = question.status,
        )
    }
}
