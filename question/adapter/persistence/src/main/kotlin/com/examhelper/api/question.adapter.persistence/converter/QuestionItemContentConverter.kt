package com.examhelper.api.question.adapter.persistence.converter

import com.examhelper.api.question.adapter.persistence.record.QuestionItemContentRecord
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Converter
@Component
class QuestionItemContentConverter(private val objectMapper: ObjectMapper) : AttributeConverter<QuestionItemContentRecord, String> {
    override fun convertToDatabaseColumn(attribute: QuestionItemContentRecord): String =
        objectMapper.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): QuestionItemContentRecord =
        objectMapper.readValue(dbData, QuestionItemContentRecord::class.java)
}
