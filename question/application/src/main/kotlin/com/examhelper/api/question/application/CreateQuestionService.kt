package com.examhelper.api.question.application

import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.QuestionId
import com.examhelper.api.question.domain.Question
import com.examhelper.api.question.port.inbound.CreateQuestionUseCase
import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand
import com.examhelper.api.question.port.inbound.result.CreateQuestionResult
import com.examhelper.api.question.port.outbound.QuestionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * CreateQuestionService는 CreateQuestionUseCase 인터페이스를 구현하여 질문 생성 로직을 처리하는 서비스 클래스입니다.
 * QuestionRepository를 주입받아 데이터베이스에 질문을 저장하고, IdGenerator를 사용하여 고유한 질문 ID를 생성합니다.
 */
@Service
class CreateQuestionService(
    private val questionRepository: QuestionRepository,
    private val idGenerator: IdGenerator
): CreateQuestionUseCase {
    @Transactional
    override fun execute(command: CreateQuestionCommand): CreateQuestionResult {
        val question = Question.create(
            id = QuestionId(idGenerator.generateId()),
            content = command.content
        )

        questionRepository.save(question)

        return CreateQuestionResult(question.id.value)
    }
}
