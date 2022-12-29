package com.dongholab.usersave.entity

import com.dongholab.usersave.r2dbc.R2dbcPersistable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_save")
data class UserSave (
    @Column("user_id")
    val userId: String,
    @Column("data_key")
    val dataKey: String?,
    @Column("data")
    val data: String?,
    @Column("private")
    val private: Boolean,
    @Column("created_date")
    var createdDate: LocalDateTime
): R2dbcPersistable<Long>() {
    @Id
    @Column("id")
    var id: Long? = null
    constructor(id: Long, userId: String, dataKey: String, data: String, private: Boolean, currentDateTime: LocalDateTime): this(userId, dataKey, data, private, currentDateTime) {
        this.id = id
        this.setId(id)
    }
}