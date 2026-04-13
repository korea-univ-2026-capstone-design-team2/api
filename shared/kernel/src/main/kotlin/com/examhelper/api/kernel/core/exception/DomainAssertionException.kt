package com.examhelper.api.kernel.core.exception

sealed class DomainAssertionException(
    override val code: String,
    override val message: String,
) : DomainException(code, message)
