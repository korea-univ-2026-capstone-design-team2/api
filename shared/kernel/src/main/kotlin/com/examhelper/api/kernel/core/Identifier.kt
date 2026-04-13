package com.examhelper.api.kernel.core

import java.io.Serializable

abstract class Identifier<T : Serializable>(val value: T) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Identifier<*>

        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value.toString()
}
