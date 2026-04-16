package com.examhelper.api.kernel.core.exception

abstract class DomainAssertionException(
    override val code: String,
    override val message: String,
) : DomainException(code, message)
