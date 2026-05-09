package com.examhelper.api.question_generation.application

import com.examhelper.api.question_generation.port.inbound.IngestFrameUseCase
import com.examhelper.api.question_generation.port.inbound.command.IngestFrameCommand
import com.examhelper.api.question_generation.port.inbound.model.LogicalFrameDocument
import com.examhelper.api.question_generation.port.outbound.IndexedFramePoint
import com.examhelper.api.question_generation.port.outbound.QdrantFrameStorePort
import com.examhelper.api.question_generation.port.outbound.TextEmbeddingPort
import com.examhelper.api.question_generation.port.outbound.command.TextEmbeddingCommand
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class IngestFrameService(
    private val retrievalTextBuilder: FrameRetrievalTextBuilder,
    private val embeddingPort: TextEmbeddingPort,
    private val qdrantFrameStorePort: QdrantFrameStorePort
) : IngestFrameUseCase {
    override fun execute(command: IngestFrameCommand) {
        val points = command.frames.map {
            val retrievalText = retrievalTextBuilder.build(it)

            val embedding = embeddingPort.embed(TextEmbeddingCommand(retrievalText))
            val frameId = buildFrameId(it)

            IndexedFramePoint(
                id = frameId,
                vector = embedding.embedding,
                payload = buildPayload(
                    frame = it,
                    frameId = frameId,
                    retrievalText = retrievalText
                )
            )
        }

        qdrantFrameStorePort.save(points)
    }

    private fun buildFrameId(frame: LogicalFrameDocument): String {
        val rawKey = buildString {
            append(frame.year)
            append("_")
            append(frame.subject)
            append("_")
            append(frame.questionNumber)
        }

        return UUID.nameUUIDFromBytes(rawKey.toByteArray()).toString()
    }

    private fun buildPayload(frame: LogicalFrameDocument, frameId: String, retrievalText: String): Map<String, Any> {
        return buildMap {
            put("frame_id", frameId)

            put("exam", frame.exam)
            put("year", frame.year)
            put("subject", frame.subject)
            put("question_number", frame.questionNumber)
            put("page", frame.page)

            put("question_type", frame.questionType)
            frame.questionSubType?.let { put("question_sub_type", it) }
            put("difficulty", frame.difficulty)
            put("topic_category", frame.topicCategory)
            frame.topicKeyword?.let { put("topic_keyword", it) }

            put("reasoning_type", frame.reasoningType)

            put("premises", frame.premises)
            put("conditions", frame.conditions)
            put("logical_goal", frame.logicalGoal)
            put("inference_structure", frame.inferenceStructure)

            put("reasoning_patterns", frame.reasoningPatterns)
            put("trap_patterns", frame.trapPatterns)
            put("discourse_structure", frame.discourseStructure)
            put("cognitive_operations", frame.cognitiveOperations)
            put("reasoning_complexity", frame.reasoningComplexity)

            put("question_stem", frame.stem)
            frame.passage?.let { put("passage", it) }
            frame.passageDescription?.let {put("passage_description", it) }

            put("answer_choices", frame.choices)
            put("correct_answer", frame.answer)
            put("correct_reason", frame.correctReason)

            put("must_preserve", frame.mustPreserve)
            put("variable_elements", frame.variableElements)

            put("retrieval_text", retrievalText)
        }
    }
}
