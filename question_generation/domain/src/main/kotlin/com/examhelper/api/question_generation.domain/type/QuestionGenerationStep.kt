package com.examhelper.api.question_generation.domain.type

enum class QuestionGenerationStep {
    FRAME_SEARCH,        // 검색
    LLM_CALL,           // AI API 호출 (배치면 인덱스별로 N건)
    QUESTION_CREATION,  // Question.create() + 저장
}
