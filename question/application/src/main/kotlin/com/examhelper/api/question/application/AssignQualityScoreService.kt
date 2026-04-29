package com.examhelper.api.question.application

import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.AssignQualityScoreUseCase
import com.examhelper.api.question.port.inbound.command.AssignQualityScoreCommand
import com.examhelper.api.question.port.inbound.result.AssignQualityScoreResult
import com.examhelper.api.question.port.outbound.QuestionStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AssignQualityScoreService(
    private val questionStore: QuestionStore,
) : AssignQualityScoreUseCase {

    @Transactional
    override fun execute(command: AssignQualityScoreCommand): AssignQualityScoreResult {
        val question = questionStore.loadById(QuestionId(command.questionId))
            ?: throw QuestionException.NotFound(command.questionId.toString())

        question.assignQualityScore(command.score)
        questionStore.save(question)

        return AssignQualityScoreResult(
            questionId = question.id.value,
            score = command.score.value,
            isPassing = command.score.isPassing(),
        )
    }
}
