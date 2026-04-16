package com.examhelper.api.infrastructure.web

import com.examhelper.api.kernel.core.exception.ErrorStatus
import org.springframework.http.HttpStatus

object ErrorStatusHttpMapper {
    fun toHttpStatus(status: ErrorStatus): HttpStatus = when (status) {
        ErrorStatus.NOT_FOUND     -> HttpStatus.NOT_FOUND
        ErrorStatus.CONFLICT      -> HttpStatus.CONFLICT
        ErrorStatus.BAD_REQUEST   -> HttpStatus.BAD_REQUEST
        ErrorStatus.UNAUTHORIZED  -> HttpStatus.UNAUTHORIZED
        ErrorStatus.FORBIDDEN     -> HttpStatus.FORBIDDEN
        ErrorStatus.INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}
