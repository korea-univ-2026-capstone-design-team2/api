package com.examhelper.api.kernel.type

enum class Subject(val korean: String) {
    VERBAL_LOGIC("언어논리") {
        override fun defaultTopicCategories() = listOf(
            "법률", "철학", "역사", "사회", "과학", "문학",
        )
    },
    DATA_INTERPRETATION("자료해석") {
        override fun defaultTopicCategories() = listOf(
            "경제통계", "인구통계", "환경지표", "예산현황",
        )
    },
    SITUATIONAL_JUDGMENT("상황판단") {
        override fun defaultTopicCategories() = listOf(
            "행정절차", "공공정책", "조직관리", "법령해석",
        )
    };

    abstract fun defaultTopicCategories(): List<String>
}
