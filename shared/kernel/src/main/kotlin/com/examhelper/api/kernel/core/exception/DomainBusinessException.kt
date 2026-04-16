package com.examhelper.api.kernel.core.exception

abstract class DomainBusinessException(
    override val code: String,
    override val message: String,
    val status: ErrorStatus,
) : DomainException(code, message)
