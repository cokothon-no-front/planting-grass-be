package com.dongholab.usersave.controller

import com.dongholab.usersave.domain.account.AuthLoginModel
import com.dongholab.usersave.domain.account.AuthReqModel
import com.dongholab.usersave.domain.account.JWTToken
import com.dongholab.usersave.domain.common.DataPage
import com.dongholab.usersave.domain.user.UserSaveData
import com.dongholab.usersave.entity.User
import com.dongholab.usersave.entity.UserSave
import com.dongholab.usersave.security.JwtUtil
import com.dongholab.usersave.service.UserService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/sign-in")
    fun signIn(@RequestBody authLoginModel: AuthLoginModel): Mono<ResponseEntity<JWTToken>> {
        return userService.loginCheck(authLoginModel).map {
            jwtUtil.generateToken(it).let {
                ResponseEntity.ok(JWTToken(it))
            }
        }?.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
    }

    @PutMapping("/sign-up")
    fun signUp(@RequestBody authReqModel: AuthReqModel): ResponseEntity<Mono<User>> {
        return ResponseEntity.ok(userService.signUp(authReqModel))
    }

    @GetMapping
    fun getMyself(principal: Principal): Mono<ResponseEntity<User>> {
        return userService.getUser(principal.name)
            .mapNotNull { ResponseEntity.ok(it) }
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
    }

    @GetMapping("/list")
    fun getList(
        @RequestParam("email_query") emailQuery: String
    ): ResponseEntity<Flux<User>> {
        return ResponseEntity.ok(
            userService.findAllByUserIdLike(emailQuery)
        )
    }

    @PostMapping("/save")
    fun userSave(
        principal: Principal,
        @RequestBody data: UserSaveData
    ): ResponseEntity<Mono<UserSave>> = ResponseEntity.ok(userService.userSave(principal, data))

    @GetMapping("/save/{id}")
    fun userSaveGet(
        principal: Principal?,
        @PathVariable("id") id: Long
    ): ResponseEntity<Mono<UserSave?>> = ResponseEntity.ok(userService.userSaveGet(principal, id))

    @PutMapping("/save/{id}")
    fun userSaveEdit(
        principal: Principal,
        @PathVariable("id") id: Long,
        @RequestBody data: UserSaveData
    ): ResponseEntity<Mono<UserSave>> = ResponseEntity.ok(userService.userSaveEdit(principal, id, data))

    @DeleteMapping("/save/{id}")
    fun userSaveDelete(
        principal: Principal,
        @PathVariable("id") id: Long
    ): ResponseEntity<Mono<UserSave>> = ResponseEntity.ok(userService.userSaveDelete(principal, id))

    @PostMapping("/save/filter/pageable")
    fun userSaveFilterPageable(
        principal: Principal?,
        @RequestParam("key") dataKeyLike: String,
        pageable: Pageable
    ): ResponseEntity<Mono<DataPage<List<UserSave>>>> {
        return ResponseEntity.ok(userService.userSaveListWithFilterPageable(principal, dataKeyLike, pageable))
    }

    @PostMapping("/save/filter")
    fun userSaveFilter(
        principal: Principal?,
        @RequestParam("key") dataKeyLike: String,
        pageable: Pageable
    ): ResponseEntity<Flux<UserSave>> {
        return ResponseEntity.ok(userService.userSaveListWithFilter(principal, dataKeyLike, pageable))
    }

    @PostMapping("/save/filter/count")
    fun userSaveCountFilter(
        principal: Principal?,
        @RequestParam("key") dataKeyLike: String
    ): ResponseEntity<Mono<Long>> {
        return ResponseEntity.ok(userService.userSaveCountFilter(principal, dataKeyLike))
    }

    @GetMapping("/save")
    fun userSaveList(
        principal: Principal
    ): ResponseEntity<Flux<UserSave>> = ResponseEntity.ok(userService.userSaveList(principal))
}