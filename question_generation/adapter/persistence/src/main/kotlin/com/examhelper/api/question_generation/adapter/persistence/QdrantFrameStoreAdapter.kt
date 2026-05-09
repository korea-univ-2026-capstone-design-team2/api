package com.examhelper.api.question_generation.adapter.persistence

import com.examhelper.api.question_generation.port.outbound.IndexedFramePoint
import com.examhelper.api.question_generation.port.outbound.QdrantFrameStorePort
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Repository

@Repository
class QdrantFrameStoreAdapter(
    private val vectorStore: VectorStore
) : QdrantFrameStorePort {
    override fun save(points: List<IndexedFramePoint>) {
        val documents = points.map {
            Document.builder()
                .id(it.id)
                .text(
                    it.payload["retrieval_text"] as String
                )
                .metadata(it.payload)
                .build()
        }

        vectorStore.add(documents)
    }
}
