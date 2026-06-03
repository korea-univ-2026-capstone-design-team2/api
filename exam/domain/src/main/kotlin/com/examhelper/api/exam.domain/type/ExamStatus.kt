package com.examhelper.api.exam.domain.type

enum class ExamStatus {
    GENERATING,  // AI 문항 생성 중
    READY,       // 생성 완료, 즉시 응시 가능
    FAILED,      // 생성 실패
}
