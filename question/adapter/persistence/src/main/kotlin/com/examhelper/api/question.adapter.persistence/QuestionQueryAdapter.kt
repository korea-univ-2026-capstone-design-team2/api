package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.question.adapter.persistence.record.AnswerChoiceRecord
import com.examhelper.api.question.domain.exception.QuestionException
import com.examhelper.api.question.port.inbound.view.AnswerChoiceView
import com.examhelper.api.question.port.inbound.view.AnswerChoiceViewWithAnswer
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionPropositionView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import com.examhelper.api.question.port.inbound.view.QuestionSummaryView
import com.examhelper.api.question.port.outbound.QuestionFilter
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class QuestionQueryAdapter(
    private val questionJpaReader: QuestionJpaReader,
) : QuestionReader {
    // ── QuestionReader 구현 ────────────────────────────────
    override fun findPaperById(id: Long): QuestionPaperView? =
        questionJpaReader.findEntityById(id)?.toPaperView()

    override fun findReviewById(id: Long): QuestionReviewView? =
        questionJpaReader.findEntityById(id)?.toReviewView()

    override fun findDetailById(id: Long): QuestionDetailView? =
        questionJpaReader.findEntityById(id)?.toDetailView()

    override fun findAll(filter: QuestionFilter): List<QuestionSummaryView> =
        questionJpaReader.findSummaries(
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty,
            status = filter.status,
            pageable = PageRequest.of(filter.page, filter.size),
        )

    override fun count(filter: QuestionFilter): Long =
        questionJpaReader.countByFilter(
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty,
            status = filter.status,
        )
}

// ── Entity → PaperView (정답/해설 제외) ───────────────────
private fun QuestionEntity.toPaperView(): QuestionPaperView {
    return QuestionPaperView(
        questionId = id,
        questionSetId = questionSetId,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        stem = content.stem,
        passageType = content.passage?.type,
        passageContent = content.passage?.content,
        exhibitType = content.exhibit?.type,
        exhibitContent = content.exhibit?.content,
        propositions = content.exhibit?.propositions?.map {
            QuestionPropositionView(
                label = it.label,
                content = it.content
            )
        },
        answerSheetType = answerSheet.type,
        choices = answerSheet.choices?.map {
            AnswerChoiceView(
                number = it.number,
                text = it.toDisplayText()
            )
        } ?: emptyList(),
    )
}

// ── Entity → ReviewView (정답 + 해설 포함) ────────────────
private fun QuestionEntity.toReviewView(): QuestionReviewView {
    return QuestionReviewView(
        questionId = id,
        questionSetId = questionSetId,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        stem = content.stem,
        passageType = content.passage?.type,
        passageContent = content.passage?.content,
        exhibitType = content.exhibit?.type,
        exhibitContent = content.exhibit?.content,
        propositions = content.exhibit?.propositions?.map {
            QuestionPropositionView(
                label = it.label,
                content = it.content
            )
        },
        answerSheetType = answerSheet.type,
        correctNumber = answerSheet.correctNumber,
        choices = answerSheet.choices?.map {
            AnswerChoiceViewWithAnswer(
                number = it.number,
                text = it.toDisplayText(),
                isCorrect = it.isCorrect,
            )
        } ?: emptyList(),
        correctReason = explanation.correctReason,
        incorrectReasons = explanation.incorrectReasons,
    )
}

// ── Entity → DetailView (내부 관리용, 전체 포함) ──────────
private fun QuestionEntity.toDetailView(): QuestionDetailView {
    return QuestionDetailView(
        questionId = id,
        generationId = generationId,
        questionSetId = questionSetId,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        status = status,
        qualityScore = qualityScore,
        passageTopicCategory = passageTopic?.category,
        passageTopicKeyword = passageTopic?.keyword,
        stem = content.stem,
        passageType = content.passage?.type,
        passageContent = content.passage?.content,
        exhibitType = content.exhibit?.type,
        exhibitContent = content.exhibit?.content,
        propositions = content.exhibit?.propositions?.map {
            QuestionPropositionView(
                label = it.label,
                content = it.content
            )
        },
        answerSheetType = answerSheet.type,
        correctNumber = answerSheet.correctNumber,
        choices = answerSheet.choices?.map {
            AnswerChoiceViewWithAnswer(
                number = it.number,
                text = it.toDisplayText(),
                isCorrect = it.isCorrect,
            )
        } ?: emptyList(),
        correctReason = explanation.correctReason,
        incorrectReasons = explanation.incorrectReasons,
        frameId = sourceFrame.frameId,
        similarityScore = sourceFrame.similarityScore,
        frameType = sourceFrame.frameType,
    )
}

private fun AnswerChoiceRecord.toDisplayText(): String = when (type) {
    "TEXT" -> content ?: throw QuestionException.AnswerChoiceBlank()
    "PROPOSITION_COMBINATION" ->
        labels?.joinToString(", ") { it } ?: throw QuestionException.AnswerChoiceBlank()
    else -> error("Unknown AnswerChoice type: $type")
}
