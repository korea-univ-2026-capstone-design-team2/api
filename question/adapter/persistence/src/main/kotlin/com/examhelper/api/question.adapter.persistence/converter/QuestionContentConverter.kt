package com.examhelper.api.question.adapter.persistence.converter

import com.examhelper.api.question.adapter.persistence.record.QuestionContentRecord
import com.examhelper.api.question.domain.vo.QuestionContent
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Converter
@Component
class QuestionContentConverter(private val objectMapper: ObjectMapper) : AttributeConverter<QuestionContentRecord, String> {
    override fun convertToDatabaseColumn(attribute: QuestionContentRecord): String =
        objectMapper.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): QuestionContentRecord =
        objectMapper.readValue(dbData, QuestionContentRecord::class.java)
}
