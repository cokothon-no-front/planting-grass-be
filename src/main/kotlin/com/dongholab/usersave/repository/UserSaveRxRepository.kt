package com.dongholab.usersave.repository

import com.dongholab.usersave.entity.UserSave
import com.dongholab.usersave.r2dbc.R2dbcPersistableRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface UserSaveRxRepository: R2dbcPersistableRepository<UserSave, Long> {
    fun findAllByUserId(userId: String): Flux<UserSave>

    fun findAllByDataKeyLikeAndPrivateIs(dataKeyLike: String, private: Boolean): Flux<UserSave>

    fun findAllByDataKeyLikeAndPrivateIsAndUserId(dataKeyLike: String, private: Boolean, userId: String): Flux<UserSave>

    fun findAllByDataKeyLikeAndUserIdOrDataKeyLikeAndPrivate(dataKey1: String, userId: String?, dataKey2: String, private: Boolean, pageable: Pageable): Flux<UserSave>
    fun countByDataKeyLikeAndUserIdOrDataKeyLikeAndPrivate(dataKey1: String, userId: String?, dataKey2: String, private: Boolean): Mono<Long>

    @Query(
        "SELECT * FROM user_save WHERE data_key LIKE :data_key_like AND (user_id = :user_id OR private = :private)"
    )
    fun findAllByFilter(
        @Param("user_id") userId: String,
        @Param("data_key_like") dataKeyLike: String,
        @Param("private") private: Boolean
    ): Flux<UserSave>
}