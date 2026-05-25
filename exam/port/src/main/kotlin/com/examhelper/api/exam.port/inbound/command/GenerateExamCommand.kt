package com.examhelper.api.exam.port.inbound.command

import com.examhelper.api.exam.domain.type.ExamTopic
import com.examhelper.api.exam.domain.vo.ExamMetadata
import com.examhelper.api.kernel.type.DifficultyLevel
import com.examhelper.api.kernel.type.QuestionSubType
import com.examhelper.api.kernel.type.QuestionType
import com.examhelper.api.kernel.type.Subject
import com.examhelper.api.kernel.type.TopicCategory

data class GenerateExamCommand(
    val title: String,
    val subject: Subject,
    val questionType: QuestionType,
    val questionSubType: QuestionSubType?,
    val difficulty: DifficultyLevel,
    val topicCategory: TopicCategory,
    val topicKeyword: String?,
    val topicDescription: String?,
    val targetQuestionCount: Int,
    val frameSearchTopK: Int = 3,
) {
    init {
        require(title.isNotBlank()) { "title must not be blank" }
        require(targetQuestionCount > 0) { "targetQuestionCount must be positive" }
    }

    fun toMetadata(): ExamMetadata = ExamMetadata(
        subject = subject,
        questionType = questionType,
        questionSubType = questionSubType,
        difficulty = difficulty,
        topic = ExamTopic(
            category = topicCategory,
            keyword = topicKeyword,
            description = topicDescription,
        ),
        targetQuestionCount = targetQuestionCount,
    )
}
