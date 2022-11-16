package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

interface AuthTokenService {
    fun generateTokenByUserId(userId: Long): AuthToken
    fun verifyToken(authToken: String): Boolean
    fun getCurrentUserId(authToken: String): Long
}

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenServiceImpl(
    private val authProperties: AuthProperties,
    private val userRepository: UserRepository,
) : AuthTokenService {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    override fun generateTokenByUserId(userId: Long): AuthToken {
        val claims: MutableMap<String, Long> = mutableMapOf("userId" to userId)
        val now = Date()
        val expiredAt = Date(now.time + authProperties.jwtExpiration)
        val resultToken = Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(claims)
            .setIssuer(authProperties.issuer)
            .setIssuedAt(now)
            .setExpiration(expiredAt)
            .signWith(signingKey)
            .compact()
        return AuthToken(tokenPrefix + resultToken)
    }

    override fun verifyToken(authToken: String): Boolean {
        try {
            parse(authToken).body
            getCurrentUserId(authToken)
        } catch (e: RuntimeException) {
            return false
        }
        return true
    }

    override fun getCurrentUserId(authToken: String): Long {
        val userId = parse(authToken).body["userId"] as? Int
            ?: throw AuthException("잘못된 아이디에 대한 토큰입니다")
        userRepository.findByIdOrNull(userId.toLong()) ?: throw AuthException("잘못된 아이디에 대한 토큰입니다")
        return userId.toLong()
    }

    private fun parse(authToken: String): Jws<Claims> {
        val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(prefixRemoved)
    }
}