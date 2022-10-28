package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.UserPort
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
    private val userPort: UserPort,
) {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    fun generateTokenByEmail(email: String): AuthToken {
        val claims: MutableMap<String, Any> = mutableMapOf("email" to email)
        val issuer = authProperties.issuer
        val expiryDate: Date = Date.from(
            LocalDateTime
                .now()
                .plusSeconds(authProperties.jwtExpiration)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )
        val resultToken = Jwts.builder()
            .setClaims(claims)
            .setIssuer(issuer)
            .setExpiration(expiryDate)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()

        return AuthToken(resultToken)
    }

    fun verifyToken(authToken: String) {
        TODO()
    }

    fun getCurrentUserId(authToken: String): Long {
        TODO()
    }

    /**
     * TODO Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
     *   적절한 인증 처리가 가능하도록 구현해주세요!
     */
    private fun parse(authToken: String): Jws<Claims> {
        val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts.parserBuilder().build().parseClaimsJws(prefixRemoved)
    }

    fun signUp(signUpRequest: SignUpRequest): AuthToken {
        val user = userPort.createUser(signUpRequest)
        return generateTokenByEmail(user.email)
    }

    fun login(loginRequest: LoginRequest): AuthToken {
        val user = userPort.getUser(loginRequest)
        return generateTokenByEmail(user.email)
    }
}