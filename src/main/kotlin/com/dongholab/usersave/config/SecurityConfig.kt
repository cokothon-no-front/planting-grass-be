package com.dongholab.usersave.config

import com.dongholab.usersave.constants.SecurityConstants.localhost
import com.dongholab.usersave.security.AuthenticationManager
import com.dongholab.usersave.security.SecurityContextRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    val authenticationManager: AuthenticationManager,
    val securityContextRepository: SecurityContextRepository
) {
    @Autowired
    lateinit var commonServerAuthenticationEntryPoint: CommonServerAuthenticationEntryPoint

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.cors()
            .and()
            .authorizeExchange()
            .pathMatchers("/admin/**").hasRole("ADMIN") // 어드민만 접근 가능
            .pathMatchers("/user/signup").permitAll()
            .pathMatchers("/user/login").permitAll()
            .pathMatchers("/user")
            .permitAll()
            .pathMatchers("/**").permitAll()
            .and()
            .httpBasic()
            .disable()
            .csrf()
            .disable()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .exceptionHandling {
                it.authenticationEntryPoint(commonServerAuthenticationEntryPoint)
            }
            .formLogin()
            .disable()
            .logout()
            .disable()
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowedOrigins = listOf(
                localhost(3000),
                localhost(3001),
                localhost(8080)
            )
            allowedMethods = listOf("*")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600
        })
    }
}