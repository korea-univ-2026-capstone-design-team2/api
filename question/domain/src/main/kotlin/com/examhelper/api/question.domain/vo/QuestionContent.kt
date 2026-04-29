package com.examhelper.api.question.domain.vo

import com.examhelper.api.question.domain.exception.QuestionAssertionException

data class QuestionContent(
    val stem: String,         // 질문 본문. ex) "다음 중 옳지 않은 것은?"
    val passage: Passage?,    // 지문 (없는 문제도 있음)
    val exhibit: Exhibit?,    // 보기 (없는 문제도 있음)
) {
    init {
        if (stem.isBlank()) throw QuestionAssertionException.StemBlank()
    }
}
