package com.examhelper.api.question.adapter.persistence.converter

import com.examhelper.api.question.adapter.persistence.record.AnswerSheetRecord
import com.examhelper.api.question.domain.vo.AnswerSheet
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Converter
@Component
class AnswerSheetConverter(private val objectMapper: ObjectMapper) : AttributeConverter<AnswerSheetRecord, String> {
    override fun convertToDatabaseColumn(attribute: AnswerSheetRecord): String =
        objectMapper.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): AnswerSheetRecord =
        objectMapper.readValue(dbData, AnswerSheetRecord::class.java)
}
