package com.examhelper.api.question_generation.port.outbound

import com.examhelper.api.question_generation.port.outbound.query.FrameSearchQuery
import com.examhelper.api.question_generation.port.outbound.result.FrameSearchResult

interface FrameSearchPort {
    fun search(query: FrameSearchQuery): List<FrameSearchResult>
}
