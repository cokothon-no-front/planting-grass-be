package com.dongholab.usersave.domain.account

data class AuthLoginModel(
    override val id: String,
    override var password: String
): AuthBase(id, password)