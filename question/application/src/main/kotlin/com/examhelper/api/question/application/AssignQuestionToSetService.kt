package com.examhelper.api.question.application

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionSetId
import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.AssignQuestionToSetUseCase
import com.examhelper.api.question.port.inbound.command.AssignQuestionToSetCommand
import com.examhelper.api.question.port.inbound.result.AssignQuestionToSetResult
import com.examhelper.api.question.port.outbound.QuestionStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/*
@Service
class AssignQuestionToSetService(
    private val questionStore: QuestionStore,
    private val questionSetRepository: QuestionSetRepository,
) : AssignQuestionToSetUseCase {

    @Transactional
    override fun execute(command: AssignQuestionToSetCommand): AssignQuestionToSetResult {
        val question = questionStore.loadById(QuestionId(command.questionId))
            ?: throw QuestionException.NotFound(command.questionId.toString())

        val questionSet = questionSetRepository.loadById(QuestionSetId(command.questionSetId))
            ?: throw QuestionException.NotFound(command.questionSetId.toString())

        question.assignToSet(questionSet.id)
        questionSet.addQuestion(question.id)

        questionStore.save(question)
        questionSetRepository.save(questionSet)

        return AssignQuestionToSetResult(
            questionId = question.id.value,
            questionSetId = questionSet.id.value,
        )
    }
}
*/
