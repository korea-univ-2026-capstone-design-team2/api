package com.examhelper.api.question_generation.port.outbound.result

import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.TopicCategory

data class FrameSearchResult(
    val frameId: String,
    val similarityScore: Double,

    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,

    val topicCategory: TopicCategory,
    val topicKeyword: String?,

    val reasoningType: String,

    val premises: List<String>,
    val conditions: List<String>,
    val logicalGoal: String,
    val inferenceStructure: List<String>,

    val reasoningPatterns: List<String>,
    val trapPatterns: List<String>,
    val discourseStructure: List<String>,
    val cognitiveOperations: List<String>,

    val reasoningComplexity: String,

    val questionStem: String,
    val passage: String?,
    val passageDescription: String?,

    val answerChoices: List<String>,

    val correctAnswer: Int,
    val correctReason: String,

    val mustPreserve: List<String>,
    val variableElements: List<String>,

    val retrievalText: String
)
