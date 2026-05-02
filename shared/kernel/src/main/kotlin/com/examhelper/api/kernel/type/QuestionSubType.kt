package com.examhelper.api.kernel.type

enum class QuestionSubType {
    // 독해형 하위 유형
    MATCH,                   // 일치 부합
    KNOWABLE,                // 알 수 있냐 없냐
    CONTEXT_CORRECTION,      // 문맥 수정
    BLANK_FILLING,           // 빈칸에 들어갈 말
    CORE_ARGUMENT,           // 핵심 논지
    INFERENCE,               // 추론 가능/불가능

    // 논증형 하위 유형
    ARGUMENT_ANALYSIS,       // 논증 분석
    STRENGTHEN_WEAKEN,       // 강화/약화
}
