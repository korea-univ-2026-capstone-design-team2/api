package com.examhelper.api.exam.application

import com.examhelper.api.exam.domain.exception.ExamException
import com.examhelper.api.exam.port.inbound.GetExamDetailUseCase
import com.examhelper.api.exam.port.inbound.query.GetExamDetailQuery
import com.examhelper.api.exam.port.inbound.view.ExamDetailView
import com.examhelper.api.exam.port.inbound.view.ExamItemView
import com.examhelper.api.exam.port.outbound.ExamReader
import com.examhelper.api.question.port.outbound.QuestionReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetExamDetailService(
    private val examReader: ExamReader,
    private val questionReader: QuestionReader,
) : GetExamDetailUseCase {

    override fun execute(
        query: GetExamDetailQuery
    ): ExamDetailView {

        val exam =
            examReader.findBaseById(query.examId)
                ?: throw ExamException.NotFound(query.examId)

        val items = exam.generationId?.let { generationId ->
            questionReader.findPapersByGenerationId(generationId)
                .mapIndexed { index, question ->

                    ExamItemView(
                        examItemId = question.questionId, // 임시
                        questionId = question.questionId,
                        ordering = index + 1,
                    )
                }
        }
            ?: emptyList()

        return ExamDetailView(
            examId = exam.examId,
            title = exam.title,
            subject = exam.subject,
            questionType = exam.questionType,
            questionSubType = exam.questionSubType,
            difficulty = exam.difficulty,
            topicCategory = exam.topicCategory,
            topicKeyword = exam.topicKeyword,
            topicDescription = exam.topicDescription,
            targetQuestionCount = exam.targetQuestionCount,
            status = exam.status,
            generationId = exam.generationId,
            generationSuccessCount = exam.generationSuccessCount,
            generationFailCount = exam.generationFailCount,
            items = items,
            createdAt = exam.createdAt,
            updatedAt = exam.updatedAt,
        )
    }
}
