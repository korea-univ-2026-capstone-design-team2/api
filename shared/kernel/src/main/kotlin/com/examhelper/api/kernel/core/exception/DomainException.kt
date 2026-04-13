package com.examhelper.api.kernel.core.exception

sealed class DomainException(
    open val code: String,
    override val message: String,
) : RuntimeException(message)
