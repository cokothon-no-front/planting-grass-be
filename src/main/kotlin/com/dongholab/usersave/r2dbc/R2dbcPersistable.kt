package com.dongholab.usersave.r2dbc

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Persistable

abstract class R2dbcPersistable<T>: Persistable<T> {
    private var id: T? = null

    override fun getId(): T? {
        if (this.id != null) {
            return this.id!!
        } else {
            return null
        }
    }

    fun setId(id: T) {
        this.id = id
    }

    @Transient
    @Value("null")
    private var newProduct: Boolean = false

    fun setNew() {
        this.newProduct = true
    }

    @JsonIgnore
    override fun isNew(): Boolean = this.newProduct || id == null;
}