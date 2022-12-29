package com.dongholab.usersave.domain.user

data class UserSaveData(
    val dataKey: String,
    val data: String,
    val private: Boolean = false
)
