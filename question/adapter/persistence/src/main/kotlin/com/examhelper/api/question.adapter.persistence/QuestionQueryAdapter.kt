package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.question.adapter.persistence.projection.CorrectAnswerProjection
import com.examhelper.api.question.adapter.persistence.record.AnswerChoiceRecord
import com.examhelper.api.question.adapter.persistence.record.SharedQuestionContextRecord
import com.examhelper.api.question.domain.exception.QuestionItemException
import com.examhelper.api.question.port.inbound.view.AnswerChoiceView
import com.examhelper.api.question.port.inbound.view.AnswerChoiceViewWithAnswer
import com.examhelper.api.question.port.inbound.view.CorrectAnswerView
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemPaperView
import com.examhelper.api.question.port.inbound.view.QuestionItemPropositionView
import com.examhelper.api.question.port.inbound.view.QuestionItemReviewView
import com.examhelper.api.question.port.inbound.view.QuestionItemSummaryView
import com.examhelper.api.question.port.inbound.view.QuestionPaperView
import com.examhelper.api.question.port.inbound.view.QuestionReviewView
import com.examhelper.api.question.port.inbound.view.QuestionSummaryView
import com.examhelper.api.question.port.outbound.QuestionFilter
import com.examhelper.api.question.port.outbound.QuestionItemFilter
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

@Repository
class QuestionQueryAdapter(
    private val questionJpaReader: QuestionJpaReader,
    private val questionItemJpaReader: QuestionItemJpaReader,
) : QuestionReader {

    // ── Question 단위 ──────────────────────────────────────
    override fun findPaperById(id: Long): QuestionPaperView? {
        val question = questionJpaReader.findEntityById(id) ?: return null
        val items = questionItemJpaReader.findAllByQuestionId(id)
        return question.toPaperView(items)
    }

    override fun findPapersByIds(ids: List<Long>): List<QuestionPaperView> {
        if (ids.isEmpty()) return emptyList()

        val questionsById = questionJpaReader.findAllByIdIn(ids).associateBy { it.id }
        val itemsByQuestionId = questionItemJpaReader.findAllByQuestionIds(ids).groupBy { it.question.id }

        return ids.mapNotNull {
            val question = questionsById[it] ?: return@mapNotNull null
            question.toPaperView(itemsByQuestionId[it] ?: emptyList())
        }
    }

    override fun findReviewById(id: Long): QuestionReviewView? {
        val question = questionJpaReader.findEntityById(id) ?: return null
        val items = questionItemJpaReader.findAllByQuestionId(id)
        return question.toReviewView(items)
    }

    override fun findReviewsByIds(ids: List<Long>): List<QuestionReviewView> {
        if (ids.isEmpty()) return emptyList()

        val questionsById = questionJpaReader.findAllByIdIn(ids).associateBy { it.id }
        val itemsByQuestionId = questionItemJpaReader.findAllByQuestionIds(ids).groupBy { it.question.id }

        return ids.mapNotNull {
            val question = questionsById[it] ?: return@mapNotNull null
            question.toReviewView(itemsByQuestionId[it] ?: emptyList())
        }
    }

    override fun findDetailById(id: Long): QuestionDetailView? {
        val question = questionJpaReader.findEntityById(id) ?: return null
        val items = questionItemJpaReader.findAllByQuestionId(id)
        return question.toDetailView(items)
    }

    override fun findAll(filter: QuestionFilter): List<QuestionSummaryView> =
        questionJpaReader.findSummaries(
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty,
            pageable = PageRequest.of(filter.page, filter.size),
        )

    override fun count(filter: QuestionFilter): Long =
        questionJpaReader.countByFilter(
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty
        )

    // ── QuestionItem 단위 ──────────────────────────────────
    override fun findItemPaperById(id: Long): QuestionItemPaperView? =
        questionItemJpaReader.findEntityById(id)?.toItemPaperView()

    override fun findItemReviewById(id: Long): QuestionItemReviewView? =
        questionItemJpaReader.findEntityById(id)?.toItemReviewView()

    override fun findItemDetailById(id: Long): QuestionItemDetailView? =
        questionItemJpaReader.findEntityById(id)?.toItemDetailView()

    override fun findAllItems(filter: QuestionItemFilter): List<QuestionItemSummaryView> =
        questionItemJpaReader.findSummaries(
            subject = filter.subject,
            questionType = filter.questionType,
            questionSubType = filter.questionSubType,
            difficulty = filter.difficulty,
            questionId = filter.questionId,
            pageable = PageRequest.of(filter.page, filter.size),
        )

    override fun countItems(filter: QuestionItemFilter): Long =
        questionItemJpaReader.countByFilter(
            subject = filter.subject,
            questionType = filter.questionType,
            questionSubType = filter.questionSubType,
            difficulty = filter.difficulty,
            questionId = filter.questionId,
        )

    override fun findCorrectAnswersByQuestionItemIds(questionItemIds: List<Long>): List<CorrectAnswerView> {
        return questionItemJpaReader.findCorrectAnswersByIds(questionItemIds).map { it.toView() }
    }
}

// ── QuestionEntity → PaperView ────────────────────────────
private fun QuestionEntity.toPaperView(
    items: List<QuestionItemEntity>,
): QuestionPaperView {
    val orderedItems = items.sortedBy { it.id }
    val (contextContent, contextDescription) = sharedContext.toContentPair()
    return QuestionPaperView(
        questionId = id,
        generationId = generationId,
        subject = subject,
        questionType = questionType,
        difficulty = difficulty,
        sharedContextContent = contextContent,
        sharedContextDescription = contextDescription,
        items = orderedItems.map { it.toItemPaperView() },
    )
}

// ── QuestionEntity → ReviewView ───────────────────────────
private fun QuestionEntity.toReviewView(
    items: List<QuestionItemEntity>,
): QuestionReviewView {
    val orderedItems = items.sortedBy { items.indexOf(it) }
    val (contextContent, contextDescription) = sharedContext.toContentPair()
    return QuestionReviewView(
        questionId = id,
        generationId = generationId,
        subject = subject,
        questionType = questionType,
        difficulty = difficulty,
        sharedContextContent = contextContent,
        sharedContextDescription = contextDescription,
        items = orderedItems.map { it.toItemReviewView() },
    )
}

// ── QuestionEntity → DetailView ───────────────────────────
private fun QuestionEntity.toDetailView(
    items: List<QuestionItemEntity>,
): QuestionDetailView {
    val orderedItems = items.sortedBy { items.indexOf(it) }
    val (contextContent, contextDescription) = sharedContext.toContentPair()
    return QuestionDetailView(
        questionId = id,
        generationId = generationId,
        subject = subject,
        questionType = questionType,
        difficulty = difficulty,
        status = status,
        passageTopicCategory = passageTopic?.category,
        passageTopicKeyword = passageTopic?.keyword,
        sharedContextContent = contextContent,
        sharedContextDescription = contextDescription,
        items = orderedItems.map { it.toItemDetailView() },
    )
}

// ── QuestionItemEntity → ItemPaperView ────────────────────
private fun QuestionItemEntity.toItemPaperView(): QuestionItemPaperView =
    QuestionItemPaperView(
        questionItemId = id,
        questionId = question.id,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        stem = content.stem,
        exhibitType = content.exhibit?.type,
        exhibitContent = content.exhibit?.content,
        propositions = content.exhibit?.propositions?.map {
            QuestionItemPropositionView(label = it.label, content = it.content)
        },
        answerSheetType = answerSheet.type,
        choices = answerSheet.choices?.map {
            AnswerChoiceView(number = it.number, text = it.toDisplayText())
        } ?: emptyList(),
    )

// ── QuestionItemEntity → ItemReviewView ───────────────────
private fun QuestionItemEntity.toItemReviewView(): QuestionItemReviewView =
    QuestionItemReviewView(
        questionItemId = id,
        questionId = question.id,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        stem = content.stem,
        exhibitType = content.exhibit?.type,
        exhibitContent = content.exhibit?.content,
        propositions = content.exhibit?.propositions?.map {
            QuestionItemPropositionView(label = it.label, content = it.content)
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

// ── QuestionItemEntity → ItemDetailView ───────────────────
private fun QuestionItemEntity.toItemDetailView(): QuestionItemDetailView =
    QuestionItemDetailView(
        questionItemId = id,
        questionId = question.id,
        generationId = generationId,
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        status = status,
        qualityScore = qualityScore,
        stem = content.stem,
        exhibitType = content.exhibit?.type,
        exhibitContent = content.exhibit?.content,
        propositions = content.exhibit?.propositions?.map {
            QuestionItemPropositionView(label = it.label, content = it.content)
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

// ── 공통 ─────────────────────────────────────────────────
private fun SharedQuestionContextRecord?.toContentPair(): Pair<String?, String?> {
    if (this == null) return null to null
    return when (this) {
        is SharedQuestionContextRecord.Text -> this.content to this.description
    }
}

private fun AnswerChoiceRecord.toDisplayText(): String = when (type) {
    "TEXT" -> content ?: throw QuestionItemException.AnswerChoiceBlank()
    "PROPOSITION_COMBINATION" ->
        labels?.joinToString(", ") ?: throw QuestionItemException.AnswerChoiceBlank()
    else -> error("Unknown AnswerChoice type: $type")
}

private fun CorrectAnswerProjection.toView(): CorrectAnswerView =
    CorrectAnswerView(
        questionItemId = questionItemId,
        correctNumber = correctNumber
    )
