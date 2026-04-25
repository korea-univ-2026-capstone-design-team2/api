package com.examhelper.api.question.adapter.persistence.converter

import com.examhelper.api.question.adapter.persistence.record.FrameReferenceRecord
import com.examhelper.api.question.domain.vo.FrameReference
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Converter
@Component
class FrameReferenceConverter(private val objectMapper: ObjectMapper) : AttributeConverter<FrameReferenceRecord, String> {
    override fun convertToDatabaseColumn(attribute: FrameReferenceRecord): String =
        objectMapper.writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String): FrameReferenceRecord =
        objectMapper.readValue(dbData, FrameReferenceRecord::class.java)
}
