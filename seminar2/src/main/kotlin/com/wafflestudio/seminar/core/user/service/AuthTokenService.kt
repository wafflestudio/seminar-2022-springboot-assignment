package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
    private val userRepository: UserRepository,
) {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    fun generateTokenByEmail(email: String): AuthToken {
        val issuedDate = LocalDateTime.now()
        val expiryDate = issuedDate.plusSeconds(authProperties.jwtExpiration)

        val resultToken = Jwts.builder()
            .setIssuer(authProperties.issuer)
            .setSubject(email)
            .setIssuedAt(Timestamp.valueOf(issuedDate))
            .setExpiration(Timestamp.valueOf(expiryDate))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()

        return AuthToken(resultToken)
    }

    fun verifyToken(authToken: String) {
        try {
            parse(authToken).body
        } catch (e: ExpiredJwtException) {
            throw AuthTokenExpiredException
        } catch (e: Exception) {
            throw InvalidTokenException
        }
    }

    fun getCurrentUserId(verifiedAuthToken: String): Long {
        return userRepository.findByEmail(parse(verifiedAuthToken).body.subject)
            ?.id
            ?: throw UserNotFoundException
    }

    private fun parse(authToken: String): Jws<Claims> {
        val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .requireIssuer(authProperties.issuer)
            .build()
            .parseClaimsJws(prefixRemoved)
    }
}