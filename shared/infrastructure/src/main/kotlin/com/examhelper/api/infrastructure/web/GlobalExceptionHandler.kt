package com.examhelper.api.infrastructure.web

import com.examhelper.api.kernel.core.exception.DomainBusinessException
import com.examhelper.api.kernel.core.exception.DomainAssertionException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger {  }

    @ExceptionHandler(DomainBusinessException::class)
    fun handleBusiness(e: DomainBusinessException): ResponseEntity<ApiResponse.Failure> {
        return ResponseEntity
            .status(ErrorStatusHttpMapper.toHttpStatus(e.status))
            .body(ApiResponse.Failure(code = e.code, message = e.message))
    }

    @ExceptionHandler(DomainAssertionException::class)
    fun handleAssertion(e: DomainAssertionException): ResponseEntity<ApiResponse.Failure> {
        logger.error("[도메인 불변식 위반] code=${e.code}, message=${e.message}", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.Failure(code = "INTERNAL_ERROR", message = "서버 내부 오류가 발생했습니다"))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse.Failure> {
        logger.error("[예상치 못한 예외]", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.Failure(code = "INTERNAL_ERROR", message = "서버 내부 오류가 발생했습니다"))
    }
}
