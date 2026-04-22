package com.examhelper.api.question.port.inbound

import com.examhelper.api.question.port.inbound.command.CreateQuestionCommand
import com.examhelper.api.question.port.inbound.result.CreateQuestionResult

/**
 * port in: 애플리케이션의 내부에서 사용되는 인터페이스를 정의하는 패키지입니다.(service의 인터페이스를 제공합니다.)
 * 외부와는 port를 통해 통신하며, 내부에서는 port를 통해 구현체를 사용합니다.(Controller는 port를 통해 사용합니다. service XXXXX)
 * CreateQuestionUseCase는 질문 생성 기능을 정의하는 인터페이스입니다.
 * execute 메서드는 CreateQuestionCommand를 입력으로 받아 CreateQuestionResult를 반환합니다.
 */
interface CreateQuestionUseCase {
    fun execute(command: CreateQuestionCommand): CreateQuestionResult
}
