package com.examhelper.api.question.adapter.persistence.converter

import com.examhelper.api.question.adapter.persistence.record.ExplanationRecord
import com.examhelper.api.question.domain.vo.Explanation
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Converter
@Component
class ExplanationConverter(private val objectMapper: ObjectMapper) : AttributeConverter<ExplanationRecord, String> {
    override fun convertToDatabaseColumn(attribute: ExplanationRecord): String =
        objectMapper.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): ExplanationRecord =
        objectMapper.readValue(dbData, ExplanationRecord::class.java)
}
