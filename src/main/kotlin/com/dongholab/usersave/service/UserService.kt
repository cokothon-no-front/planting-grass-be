package com.dongholab.usersave.service

import com.dongholab.usersave.domain.account.AuthLoginModel
import com.dongholab.usersave.domain.account.AuthReqModel
import com.dongholab.usersave.domain.common.DataPage
import com.dongholab.usersave.domain.user.UserSaveData
import com.dongholab.usersave.entity.UserSave
import com.dongholab.usersave.r2dbc.save
import com.dongholab.usersave.repository.UserRxRepository
import com.dongholab.usersave.repository.UserSaveRxRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.nio.file.attribute.UserPrincipalNotFoundException
import java.security.Principal
import java.time.LocalDateTime
import com.dongholab.usersave.entity.User as UserDTO

@Service
class UserService: ReactiveUserDetailsService {
    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var userRxRepository: UserRxRepository

    @Autowired
    lateinit var userSaveRxRepository: UserSaveRxRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun getUser(id: String): Mono<UserDTO> {
        return userRxRepository.findById(id)
    }

    fun getUser(authLoginModel: AuthLoginModel): Mono<UserDTO> = getUser(authLoginModel.id)

    fun signUp(authReqModel: AuthReqModel): Mono<UserDTO> {
        return userRxRepository.save(UserDTO(
            authReqModel.apply {
                password = passwordEncoder.encode(password)
            }
        ), true)
    }

    override fun findByUsername(id: String?): Mono<UserDetails> {
        return id?.let {
            userRxRepository.findById(id)?.map {
                User(it.id, it.password, listOf(it))
            }
        }?: Mono.empty()
    }

    fun findAllByUserIdLike(id: String): Flux<com.dongholab.usersave.entity.User> {
        return userRxRepository.findAllByIdLike(id)
    }

    fun loginCheck(authLoginModel: AuthLoginModel): Mono<UserDTO> {
        return getUser(authLoginModel.id).map {
            if (passwordEncoder.matches(authLoginModel.password, it.password)) {
                it
            } else {
                throw UserPrincipalNotFoundException("No User Found")
            }
        }
    }

    fun userSaveList(userId: String): Flux<UserSave> {
        return userSaveRxRepository.findAllByUserId(userId)
    }

    fun userSaveList(principal: Principal): Flux<UserSave> {
        return principal.name?.let {
            userSaveList(it)
        }?: Flux.empty()
    }

    fun userSaveListWithFilter(principal: Principal?, dataKeyLike: String, pageable: Pageable): Flux<UserSave> {
        return userSaveRxRepository.findAllByDataKeyLikeAndUserIdOrDataKeyLikeAndPrivate(dataKeyLike, principal?.name, dataKeyLike, false, pageable)
    }

    fun userSaveListWithFilterPageable(principal: Principal?, dataKeyLike: String, pageable: Pageable): Mono<DataPage<List<UserSave>>> {
        val userSaveListMono: Mono<List<UserSave>> = userSaveListWithFilter(principal, dataKeyLike, pageable).collectList()
        val totalMono: Mono<Long> = userSaveCountFilter(principal, dataKeyLike)

        val withTotalPage: Mono<DataPage<List<UserSave>>> = Mono.zip(userSaveListMono, totalMono).map {
            val (userSaveList: List<UserSave>, total: Long) = it
            DataPage(
                total,
                userSaveList
            )
        }
        return withTotalPage
    }

    // 캐시 필요
    fun userSaveCountFilter(principal: Principal?, dataKeyLike: String): Mono<Long> {
        return userSaveRxRepository.countByDataKeyLikeAndUserIdOrDataKeyLikeAndPrivate(dataKeyLike, principal?.name, dataKeyLike, false)
    }

    fun userSave(
        userId: String,
        userSaveData: UserSaveData
    ): Mono<UserSave> {
        val currentDateTime = LocalDateTime.now()
        val (dataKey, data, private) = userSaveData
        val tempData = UserSave(
            userId,
            dataKey,
            data,
            private,
            currentDateTime
        )
        return userSaveRxRepository.save(tempData, true)
    }

    fun userSave(
        principal: Principal,
        userSaveData: UserSaveData
    ): Mono<UserSave>? {
        log.info { "name: ${principal.name}" }
        return principal.name?.let {
            userSave(it, userSaveData)
        }?: null
    }

    fun userSaveGet(
        principal: Principal?,
        id: Long
    ): Mono<UserSave?> {
        val userSaveMono = userSaveRxRepository.findById(id)
        return userSaveMono.mapNotNull { userSave ->
            if (userSave.private) {
                if (userSave.userId == principal?.name) {
                    userSave
                } else {
                    null
                }
            } else {
                userSave
            }
        }
    }

    fun userSaveEdit(
        userId: String,
        editId: Long,
        userSaveData: UserSaveData
    ): Mono<UserSave> {
        val currentDateTime = LocalDateTime.now()
        val (dataKey, data, private) = userSaveData
        val tempData = UserSave(
            editId,
            userId,
            dataKey,
            data,
            private,
            currentDateTime
        )
        return userSaveRxRepository.save(tempData, true)
    }

    fun userSaveEdit(
        principal: Principal,
        editId: Long,
        userSaveData: UserSaveData
    ): Mono<UserSave>? {
        return principal.name?.let {
            userSaveEdit(it, editId, userSaveData)
        }?: null
    }

    fun userSaveDelete(
        userId: String,
        deleteId: Long
    ): Mono<UserSave> {
        return userSaveRxRepository.findById(deleteId).flatMap {
            if (it.userId.equals(userId)) {
                userSaveRxRepository.deleteById(deleteId).then(Mono.just(it))
            } else {
                Mono.error(Throwable("Not own this item(${deleteId})."))
            }
        }.doOnError { Throwable("Error DeleteId: ${deleteId}") }
    }

    fun userSaveDelete(
        principal: Principal,
        deleteId: Long
    ): Mono<UserSave> {
        return principal.name?.let {
            userSaveDelete(it, deleteId)
        }?.doOnError { Throwable("Error Account: ${principal.name}") } ?: Mono.error(Throwable("Error Account: ${principal.name}"))
    }
}