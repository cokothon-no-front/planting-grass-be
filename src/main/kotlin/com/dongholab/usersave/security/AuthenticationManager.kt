package com.dongholab.usersave.security

import com.dongholab.usersave.constants.ExceptionConstants
import com.dongholab.usersave.repository.UserRxRepository
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(val jwtUtil: JwtUtil, val userRxRepository: UserRxRepository) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val token = authentication?.credentials.toString()
        return Mono.justOrEmpty(authentication)
            .flatMap { jwt -> validate(token) }
            .onErrorMap { error -> IllegalArgumentException(error) }
    }

    private fun validate(token:String): Mono<Authentication> {
        val id = jwtUtil.extractAllClaims(token)?.id
        return id?.let {
            val user = userRxRepository.findById(it)
            user.mapNotNull { user ->
                if (jwtUtil.validateToken(token, user)) {
                    val authorities: MutableList<SimpleGrantedAuthority> = mutableListOf()
                    user.roleType?.let {
                        authorities.add(SimpleGrantedAuthority(it.toString()))
                    }
                    UsernamePasswordAuthenticationToken(
                        user.id,
                        user.password,
                        authorities
                    ) as Authentication
                } else {
                    null
                }!!
            }.switchIfEmpty(Mono.error(ExceptionConstants.unauthorizedException))
        }?: Mono.empty()
    }
}







