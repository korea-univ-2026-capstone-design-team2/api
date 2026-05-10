package com.examhelper.api.kernel.type

enum class TopicCategory(val korean: String) {
    POLITICS("정치"),
    ECONOMY("경제"),
    SOCIETY("사회"),
    LAW("법률"),
    HISTORY("역사"),
    PHILOSOPHY("철학"),
    SCIENCE("과학"),
    TECHNOLOGY("기술"),
    CULTURE("문화"),
    ENVIRONMENT("환경");

    companion object {
        fun fromString(value: String): TopicCategory =
            entries.find { it.name == value || it.korean == value }
                ?: throw IllegalArgumentException( "알 수 없는 주제 카테고리입니다: $value")
    }
}
