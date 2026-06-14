package com.examhelper.api.infrastructure.message

import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class KafkaMessageDeserializer(
    private val objectMapper: ObjectMapper,
) {
    fun <T> deserialize(raw: String, clazz: Class<T>): T {
        val node = objectMapper.readTree(raw)
        return if (node.isString) {
            objectMapper.readValue(node.asString(), clazz)
        } else
            objectMapper.treeToValue(node, clazz)
    }
}
