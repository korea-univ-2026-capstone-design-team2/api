package com.examhelper.api.question.adapter.persistence.converter

import com.examhelper.api.question.adapter.persistence.record.SharedQuestionContextRecord
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import tools.jackson.databind.ObjectMapper
import kotlin.jvm.java

@Converter
class SharedQuestionContextConverter(private val objectMapper: ObjectMapper) : AttributeConverter<SharedQuestionContextRecord, String> {
    override fun convertToDatabaseColumn(attribute: SharedQuestionContextRecord?): String? =
        attribute?.let { objectMapper.writeValueAsString(it) }

    override fun convertToEntityAttribute(dbData: String?): SharedQuestionContextRecord? {
        if (dbData == null) return null
        val node = objectMapper.readTree(dbData)
        val type = node.get("type")?.asString()
            ?: throw IllegalStateException("SharedQuestionContext JSON에 type 필드가 없습니다: $dbData")

        return when (type) {
            "TEXT" -> objectMapper.treeToValue(node, SharedQuestionContextRecord.Text::class.java)
            else -> throw IllegalStateException("알 수 없는 SharedQuestionContext type: $type")
        }
    }
}
