package com.examhelper.api.token_usage.adapter.web.dto.response

import com.examhelper.api.token_usage.port.inbound.result.RecordTokenUsageResult

data class RecordTokenUsageResDto(val tokenUsageId: String) {
    companion object {
        fun fromResult(result: RecordTokenUsageResult): RecordTokenUsageResDto =
            RecordTokenUsageResDto(result.tokenUsageId.toString())
    }
}
