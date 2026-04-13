package com.examhelper.api.kernel.core

import java.io.Serializable

abstract class DomainEntity<ID : Identifier<out Serializable>>(val id: ID) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as DomainEntity<*>
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
