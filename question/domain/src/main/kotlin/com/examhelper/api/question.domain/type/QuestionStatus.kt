package com.examhelper.api.question.domain.type

enum class QuestionStatus {
    DRAFT,       // 생성 직후 — 검증 전
    PUBLISHED,   // 출제 가능
    REJECTED,    // 품질 미달 또는 수동 반려
}
