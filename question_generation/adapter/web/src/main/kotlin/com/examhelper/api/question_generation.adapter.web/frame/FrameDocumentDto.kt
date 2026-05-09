package com.examhelper.api.question_generation.adapter.web.frame

import com.examhelper.api.question_generation.port.inbound.model.LogicalFrameDocument

data class FrameDocumentDto(
    val source: SourceDto,
    val metadata: MetadataDto,
    val problem: ProblemDto,
    val logicalFrame: LogicalFrameDto,

    val reasoningPatterns: List<String>,
    val trapPatterns: List<String>,
    val discourseStructure: List<String>,
    val cognitiveOperations: List<String>,

    val reasoningComplexity: String,

    val generationConstraints: GenerationConstraintsDto
) {
    fun toDocument(): LogicalFrameDocument {
        return LogicalFrameDocument(
            exam = source.exam,
            year = source.year,
            subject = source.subject,
            questionNumber = source.questionNumber,
            page = source.page,

            questionType = metadata.questionType,
            questionSubType = metadata.questionSubType,
            difficulty = metadata.difficulty,

            topicCategory = metadata.topicCategory,
            topicKeyword = metadata.topicKeyword,

            reasoningType = logicalFrame.reasoningType,
            premises = logicalFrame.premises,
            conditions = logicalFrame.conditions,
            logicalGoal = logicalFrame.goal,
            inferenceStructure = logicalFrame.inferenceStructure,

            reasoningPatterns = reasoningPatterns,
            trapPatterns = trapPatterns,
            discourseStructure = discourseStructure,
            cognitiveOperations = cognitiveOperations,

            reasoningComplexity = reasoningComplexity,

            stem = problem.stem,

            passage = problem.passage?.content,
            passageDescription = problem.passage?.description,

            choices = problem.choices
                .sortedBy { it.number }
                .map { it.content },

            answer = problem.answer,

            correctReason = problem.explanation.correctReason,

            mustPreserve = generationConstraints.mustPreserve,
            variableElements = generationConstraints.variableElements
        )
    }
}
