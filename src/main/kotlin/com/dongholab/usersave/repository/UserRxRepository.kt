package com.dongholab.usersave.repository

import com.dongholab.usersave.entity.User
import com.dongholab.usersave.r2dbc.R2dbcPersistableRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface UserRxRepository: R2dbcPersistableRepository<User, String> {
    fun findAllByIdLike(id: String): Flux<User>
}
