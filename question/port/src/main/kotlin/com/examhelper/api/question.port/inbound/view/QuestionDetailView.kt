package com.examhelper.api.question.port.inbound.view

import com.examhelper.api.question.domain.type.DifficultyLevel
import com.examhelper.api.question.domain.type.QuestionStatus
import com.examhelper.api.question.domain.type.QuestionSubType
import com.examhelper.api.question.domain.type.QuestionType
import com.examhelper.api.question.domain.type.Subject

data class QuestionDetailView(
    val questionId: Long,
    val generationId: Long,
    val questionSetId: Long?,

    // 메타데이터
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val status: QuestionStatus,
    val qualityScore: Double?,
    val passageTopicCategory: String?,
    val passageTopicKeyword: String?,

    // 문제 본문
    val stem: String,
    val passageType: String?,
    val passageContent: String?,

    // 보기
    val exhibitType: String?,
    val exhibitContent: String?,
    val propositions: List<QuestionPropositionView>?,

    // 선지 — 정답 포함
    val answerSheetType: String,
    val correctNumber: Int?,
    val choices: List<AnswerChoiceViewWithAnswer>,

    // 해설
    val correctReason: String,
    val incorrectReasons: Map<String, String>,

    // RAG 출처
    val frameId: Long,
    val similarityScore: Double,
    val frameType: String
)
