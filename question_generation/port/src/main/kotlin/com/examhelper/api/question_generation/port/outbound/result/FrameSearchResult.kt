package com.examhelper.api.question_generation.port.outbound.result

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType

data class FrameSearchResult(
    val frameId                : String,    // Qdrant point ID
    val similarityScore        : Double,
    val questionType           : QuestionType,
    val questionSubType        : QuestionSubType?,
    val difficulty             : DifficultyLevel,

    // 임베딩 대상이었던 추상화 텍스트들
    val abstractPassage        : String?,   // 추상화된 지문
    val logicalStructureSummary: String,    // 논리 구조 요약
    val argumentPattern        : String?,   // 논증 패턴 (논증형만)
    val stemTemplate           : String,    // 질문 줄기 템플릿
    val choicePattern          : String,    // 선지 구성 패턴

    // 원본 보존 (프롬프트 few-shot 참조용)
    val originalStem           : String,
    val originalPassage        : String?,
    val originalChoices        : List<String>,
    val originalExplanation    : String,
)
