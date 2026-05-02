package com.examhelper.api.question.adapter.web.request

import com.examhelper.api.kernel.identifier.LogicalFrameId
import com.examhelper.api.question.domain.type.DifficultyLevel
import com.examhelper.api.question.domain.type.QuestionSubType
import com.examhelper.api.question.domain.type.QuestionType
import com.examhelper.api.question.domain.type.Subject
import com.examhelper.api.question.domain.vo.AnswerChoice
import com.examhelper.api.question.domain.vo.AnswerSheet
import com.examhelper.api.question.domain.vo.Exhibit
import com.examhelper.api.question.domain.vo.Explanation
import com.examhelper.api.question.domain.vo.FrameReference
import com.examhelper.api.question.domain.vo.Passage
import com.examhelper.api.question.domain.vo.Proposition
import com.examhelper.api.kernel.type.PropositionLabel
import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand

data class CreateQuestionReqDto(
    val content: String
) {
    fun toCommand(): CreateQuestionCommand {
        return CreateQuestionCommand(
            generationId = 1L,
            stem = "aaa",
            passage = Passage.TextPassage(
                content = content,
            ),
            exhibit = Exhibit.PropositionExhibit(listOf(
                Proposition(
                    label = PropositionLabel.ㄱ,
                    content = "aaa"
                ),
                Proposition(
                    label = PropositionLabel.ㄴ,
                    content = "bbb"
                ),
                Proposition(
                    label = PropositionLabel.ㄷ,
                    content = "ccc"
                ),
                Proposition(
                    label = PropositionLabel.ㄹ,
                    content = "ddd"
                ),
                Proposition(
                    label = PropositionLabel.ㅁ,
                    content = "eee"
                ))
            ),
            answerSheet = AnswerSheet.MultipleChoiceSheet(
                listOf(
                    AnswerChoice.PropositionCombinationChoice(
                        number = 1,
                        listOf(PropositionLabel.ㄱ),
                        isCorrect = true
                    ),
                    AnswerChoice.PropositionCombinationChoice(
                        number = 2,
                        listOf(PropositionLabel.ㄱ, PropositionLabel.ㄴ),
                        isCorrect = false
                    ),
                    AnswerChoice.PropositionCombinationChoice(
                        number = 3,
                        listOf(PropositionLabel.ㄱ, PropositionLabel.ㄷ),
                        isCorrect = false
                    ),
                    AnswerChoice.PropositionCombinationChoice(
                        number = 4,
                        listOf(PropositionLabel.ㄹ, PropositionLabel.ㅁ),
                        isCorrect = false
                    ),
                    AnswerChoice.PropositionCombinationChoice(
                        number = 5,
                        listOf(PropositionLabel.ㄱ, PropositionLabel.ㄹ),
                        isCorrect = false
                    ),
                ),
                correctNumber = 1
            ),
            subject = Subject.VERBAL_LOGIC,
            questionType = QuestionType.READING,
            questionSubType = QuestionSubType.BLANK_FILLING,
            difficulty = DifficultyLevel.MEDIUM,
            passageTopic = null,
            explanation = Explanation(
                correctReason = "ddd",
            ),
            sourceFrame = FrameReference(
                frameId = LogicalFrameId(1L),
                similarityScore = 0.9,
                frameType = "VERBAL_LOGIC_BLANK_FILLING_FRAME",
            )
        )
    }
}
