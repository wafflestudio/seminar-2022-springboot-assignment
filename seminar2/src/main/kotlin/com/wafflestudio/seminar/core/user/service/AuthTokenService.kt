package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
    private val userRepository: UserRepository
) {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    fun generateTokenByEmail(email: String): AuthToken {
        val claims = Jwts.claims()
        claims["aud"] = userRepository.findByEmail(email)?.username ?: throw Seminar404("User not found")
        val expiryDate = Date(Date().time + authProperties.jwtExpiration)
        val resultToken = Jwts.builder()
            .signWith(signingKey)
            .setClaims(claims)
            .setSubject(email)
            .setIssuer(authProperties.issuer)
            .setExpiration(expiryDate)
            .setIssuedAt(Date())
            .compact()

        return AuthToken(resultToken)
    }

    fun verifyToken(authToken: String) {
        parse(authToken)
    }

    fun getCurrentUserId(authToken: String): Long {
        val userEmail = parse(authToken).body.subject
        return userRepository.findByEmail(userEmail)?.id ?: throw Seminar404("User Authentication Failed")
    }

    private fun parse(authToken: String): Jws<Claims> {
        val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
    }
}