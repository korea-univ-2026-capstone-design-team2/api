package com.examhelper.api.infrastructure.web

sealed class ApiResponse<out T> {
    data class Success<T>(
        val data: T,
    ) : ApiResponse<T>()

    data class Failure(
        val code: String,
        val message: String,
    ) : ApiResponse<Nothing>()
}
