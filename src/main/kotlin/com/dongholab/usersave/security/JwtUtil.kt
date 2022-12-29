package com.dongholab.usersave.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import com.dongholab.usersave.entity.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.HashMap

@Service
class JwtUtil {
    @Value("\${spring.jwt.secret}")
    lateinit var jwtKey: String

    lateinit var secretKey: String

    // 비밀키를 Base64로 인코딩.
    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(jwtKey.toByteArray())
    }

    companion object {
        private const val TOKEN_TTL = 365 * 24 * 60 * 60 * 1000L
    }

    fun extractAllClaims(token: String): Claims? {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            return null
        }
    }

    fun isTokenExpired(claims: Claims): Boolean {
        return claims.expiration.before(Date())
    }

    fun generateToken(user: User): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, user)
    }

    fun createToken(claims: Map<String, Any>, user: User): String {
        val now = Date()
        return Jwts.builder()
            .setClaims(claims)
            .setId(user.id)
            .setSubject(user.name)
            .claim("roles", listOf(user.roleType))
            .setIssuer("identity")
            .setIssuedAt(Date(now.time))
            .setExpiration(Date(now.time + TOKEN_TTL))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(token: String, userDto: User?): Boolean {
        val claims: Claims? = extractAllClaims(token)
        return claims?.let {
            (it.id.equals(userDto?.id) && !isTokenExpired(it))
        }?: false
    }

}




