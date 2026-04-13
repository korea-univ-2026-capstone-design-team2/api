package com.examhelper.api.kernel.core.exception

sealed class DomainBusinessException(
    override val code: String,
    override val message: String,
    val status: ErrorStatus,
) : DomainException(code, message)
