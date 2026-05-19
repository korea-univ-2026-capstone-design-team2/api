package com.examhelper.api.question.application

import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.QuestionGenerationId
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.kernel.identifier.QuestionItemId
import com.examhelper.api.question.domain.QuestionItem
import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.domain.vo.QuestionItemContent
import com.examhelper.api.question.domain.vo.QuestionItemMetadata
import com.examhelper.api.question.port.inbound.CreateQuestionUseCase
import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand
import com.examhelper.api.question.port.inbound.result.CreateQuestionResult
import com.examhelper.api.question.port.outbound.QuestionStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * CreateQuestionService는 CreateQuestionUseCase 인터페이스를 구현하여 질문 생성 로직을 처리하는 서비스 클래스입니다.
 * QuestionRepository를 주입받아 데이터베이스에 질문을 저장하고, IdGenerator를 사용하여 고유한 질문 ID를 생성합니다.
 */
@Service
class CreateQuestionService(
    private val questionStore: QuestionStore,
    private val idGenerator: IdGenerator,
) : CreateQuestionUseCase {

    @Transactional
    override fun execute(
        command: CreateQuestionCommand
    ): CreateQuestionResult {

        // ── Aggregate 생성 ─────────────────────────────
        val questionId = QuestionId(idGenerator.generateId())

        val question = Question.create(
            id = questionId,
            generationId = QuestionGenerationId(command.generationId),
            sharedContext = command.sharedContext,
            metadata = command.metadata,
        )

        // ── QuestionItems 생성 및 aggregate 편입 ───────
        command.questions.forEach { q ->

            val questionItem = QuestionItem.create(
                id = QuestionItemId(idGenerator.generateId()),
                generationId = QuestionGenerationId(command.generationId),

                content = QuestionItemContent(
                    stem = q.stem,
                    exhibit = q.exhibit,
                ),

                answerSheet = q.answerSheet,

                metadata = QuestionItemMetadata(
                    subject = command.metadata.subject,
                    questionType = command.metadata.questionType,
                    questionSubType = command.metadata.questionSubType,
                    difficulty = command.metadata.difficulty,
                ),

                explanation = q.explanation,
            )

            question.addItem(questionItem)
        }

        questionStore.save(question)

        // ── 결과 반환 ──────────────────────────────────
        return CreateQuestionResult(
            questionId = question.id.value,
            questionItemIds = question.items.map { it.id.value }
        )
    }
}
