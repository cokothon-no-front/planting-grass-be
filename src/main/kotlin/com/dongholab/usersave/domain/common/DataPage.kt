package com.dongholab.usersave.domain.common

data class DataPage<T>(
    val total: Long,
    val data: T
)
