package com.dongholab.usersave.entity

import com.dongholab.usersave.domain.account.AuthReqModel
import com.dongholab.usersave.domain.account.RoleType
import com.dongholab.usersave.r2dbc.R2dbcPersistable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority

@Table("user")
data class User (
    @Column("name")
    val name: String,
    @Column("password")
    val password: String,
    @Column("role_type")
    val roleType: RoleType = RoleType.ROLE_USER
): R2dbcPersistable<String>(), GrantedAuthority {
    @Id
    lateinit var id: String

    constructor(authReqModel: AuthReqModel): this(authReqModel.name, authReqModel.password, RoleType.valueOf(authReqModel.roleType)) {
        setId(authReqModel.id)
        this.id = authReqModel.id
    }

    override fun getAuthority(): String = roleType.name
}