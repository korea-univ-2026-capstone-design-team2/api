package com.examhelper.api.question_generation.port.inbound.model

data class LogicalFrameDocument(
    val exam: String,
    val year: Int,
    val subject: String,
    val questionNumber: Int,
    val page: Int,

    val questionType: String,
    val questionSubType: String?,
    val difficulty: String,

    val topicCategory: String,
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

    val stem: String,

    val passage: String?,
    val passageDescription: String?,

    val choices: List<String>,

    val answer: Int,

    val correctReason: String,

    val mustPreserve: List<String>,
    val variableElements: List<String>,
)
