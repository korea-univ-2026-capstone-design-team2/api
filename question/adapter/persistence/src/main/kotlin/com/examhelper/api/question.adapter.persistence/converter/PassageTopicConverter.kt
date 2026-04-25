package com.examhelper.api.question.adapter.persistence.converter

import com.examhelper.api.question.adapter.persistence.record.PassageTopicRecord
import com.examhelper.api.question.domain.vo.PassageTopic
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Converter
@Component
class PassageTopicConverter(private val objectMapper: ObjectMapper) : AttributeConverter<PassageTopicRecord?, String?> {
    override fun convertToDatabaseColumn(attribute: PassageTopicRecord?): String? {
        return attribute?.let { objectMapper.writeValueAsString(attribute) }
    }

    override fun convertToEntityAttribute(dbData: String?): PassageTopicRecord? {
        return dbData?.let { objectMapper.readValue(it, PassageTopicRecord::class.java) }
    }
}
