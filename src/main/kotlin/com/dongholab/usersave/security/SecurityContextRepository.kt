package com.dongholab.usersave.security

import com.dongholab.usersave.constants.ExceptionConstants
import com.dongholab.usersave.repository.UserRxRepository
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
    val authenticationManager: AuthenticationManager,
    val jwtUtil: JwtUtil,
    val userRxRepository: UserRxRepository
) : ServerSecurityContextRepository {
    companion object {
        const val bearer = "Bearer "
    }

    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
        return Mono.justOrEmpty(exchange?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION))
            .onErrorMap { ExceptionConstants.unauthorizedException }
            .filter { it.startsWith(bearer) }
            .map { it.substring(bearer.length) }
            .flatMap { jwt -> returnAuth(jwt) }
            .flatMap { auth -> authenticationManager.authenticate(auth) }
            .map { SecurityContextImpl(it) }
    }

    private fun returnAuth(token: String): Mono<UsernamePasswordAuthenticationToken> {
        val id = jwtUtil.extractAllClaims(token)?.id
        return id?.let { id ->
            userRxRepository.findById(id).map {
                val authorities: MutableList<SimpleGrantedAuthority> = mutableListOf()
                it?.let {
                    it.roleType?.let {
                        authorities.add(SimpleGrantedAuthority(it.toString()))
                    }

                    UsernamePasswordAuthenticationToken(
                        it.id,
                        token,
                        authorities
                    )
                }
            }
        } ?: Mono.empty()
    }

}









