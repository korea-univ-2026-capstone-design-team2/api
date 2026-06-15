package com.examhelper.api.question.adapter.persistence

import com.examhelper.api.question.adapter.persistence.projection.CorrectAnswerProjection
import com.examhelper.api.question.adapter.persistence.projection.QuestionItemSummaryProjection
import com.examhelper.api.question.adapter.persistence.record.AnswerChoiceRecord
import com.examhelper.api.question.adapter.persistence.record.SharedQuestionContextRecord
import com.examhelper.api.question.domain.exception.QuestionItemAssertionException
import com.examhelper.api.question.domain.exception.QuestionItemException
import com.examhelper.api.question.port.inbound.view.AnswerChoiceView
import com.examhelper.api.question.port.inbound.view.AnswerChoiceViewWithAnswer
import com.examhelper.api.question.port.inbound.view.CorrectAnswerView
import com.examhelper.api.question.port.inbound.view.QuestionDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemDetailView
import com.examhelper.api.question.port.inbound.view.QuestionItemMetadataView
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
    override fun findPaperById(id: Long, memberId: Long): QuestionPaperView? {
        val question = questionJpaReader.findEntityByIdAndMemberId(id, memberId) ?: return null
        val items = questionItemJpaReader.findAllByQuestionId(id)
        return question.toPaperView(items)
    }

    override fun findPapersByIds(ids: List<Long>, memberId: Long): List<QuestionPaperView> {
        if (ids.isEmpty()) return emptyList()

        val questionsById = questionJpaReader.findAllByIdInAndMemberId(ids, memberId)
            .associateBy { it.id }
        val itemsByQuestionId = questionItemJpaReader.findAllByQuestionIds(questionsById.keys.toList())
            .groupBy { it.question.id }

        return ids.mapNotNull { id ->
            val question = questionsById[id] ?: return@mapNotNull null
            question.toPaperView(itemsByQuestionId[id] ?: emptyList())
        }
    }

    override fun findPapersByGenerationId(generationId: Long, memberId: Long): List<QuestionPaperView> {
        val questions = questionJpaReader.findAllByGenerationIdAndMemberId(generationId, memberId)
        if (questions.isEmpty()) return emptyList()

        val questionIds = questions.map { it.id }
        val itemsByQuestionId = questionItemJpaReader.findAllByQuestionIds(questionIds)
            .groupBy { it.question.id }

        return questions.map { it.toPaperView(itemsByQuestionId[it.id] ?: emptyList()) }
    }

    // Detail — memberId 검증 제외
    override fun findReviewById(id: Long, memberId: Long): QuestionReviewView? {
        val question = questionJpaReader.findEntityByIdAndMemberId(id, memberId) ?: return null
        val items = questionItemJpaReader.findAllByQuestionId(id)
        return question.toReviewView(items)
    }

    override fun findReviewsByIds(ids: List<Long>, memberId: Long): List<QuestionReviewView> {
        if (ids.isEmpty()) return emptyList()

        val questionsById = questionJpaReader.findAllByIdInAndMemberId(ids, memberId)
            .associateBy { it.id }
        val itemsByQuestionId = questionItemJpaReader.findAllByQuestionIds(questionsById.keys.toList())
            .groupBy { it.question.id }

        return ids.mapNotNull { id ->
            val question = questionsById[id] ?: return@mapNotNull null
            question.toReviewView(itemsByQuestionId[id] ?: emptyList())
        }
    }

    // Detail — memberId 검증 제외
    override fun findDetailById(id: Long): QuestionDetailView? {
        val question = questionJpaReader.findEntityById(id) ?: return null
        val items = questionItemJpaReader.findAllByQuestionId(id)
        return question.toDetailView(items)
    }

    override fun findAll(filter: QuestionFilter): List<QuestionSummaryView> =
        questionJpaReader.findSummaries(
            memberId = filter.memberId,
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty,
            pageable = PageRequest.of(filter.page, filter.size),
        )

    override fun count(filter: QuestionFilter): Long =
        questionJpaReader.countByFilter(
            memberId = filter.memberId,
            subject = filter.subject,
            questionType = filter.questionType,
            difficulty = filter.difficulty,
        )

    override fun findItemSummaries(questionItemIds: List<Long>): List<QuestionItemMetadataView> =
        questionItemJpaReader.findItemSummaries(questionItemIds).map { it.toView() }

    override fun findCorrectAnswersByQuestionItemIds(questionItemIds: List<Long>): List<CorrectAnswerView> =
        questionItemJpaReader.findCorrectAnswersByIds(questionItemIds).map { it.toView() }
}

// ── QuestionEntity → PaperView ────────────────────────────
private fun QuestionEntity.toPaperView(items: List<QuestionItemEntity>): QuestionPaperView {
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
private fun QuestionEntity.toReviewView(items: List<QuestionItemEntity>): QuestionReviewView {
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
private fun QuestionEntity.toDetailView(items: List<QuestionItemEntity>): QuestionDetailView {
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
    "TEXT" -> content ?: throw QuestionItemAssertionException.AnswerChoiceBlank()
    "PROPOSITION_COMBINATION" ->
        labels?.joinToString(", ") ?: throw QuestionItemAssertionException.AnswerChoiceBlank()
    else -> error("Unknown AnswerChoice type: $type")
}

private fun CorrectAnswerProjection.toView(): CorrectAnswerView =
    CorrectAnswerView(
        questionItemId = questionItemId,
        correctNumber = correctNumber
    )

private fun QuestionItemSummaryProjection.toView() =
    QuestionItemMetadataView(
        questionItemId = questionItemId,
        generationId = generationId,
        subject = subject,
        questionType = questionType,
        difficulty = difficulty
    )
