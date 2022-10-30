package com.wafflestudio.seminar.core.user.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
  private val authProperties: AuthProperties,
) {
  private val tokenPrefix = "Bearer "
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

  fun generateTokenByUsername(userId: Long): AuthToken {
    val now = Date()
    val expiryDate = Date(now.time + authProperties.jwtExpiration * 1000)
    
    val resultToken = Jwts.builder()
      .claim("userId", userId)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(signingKey)
      .compact()

    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String): Boolean {
    val parseResult: Jws<Claims> = parse(authToken)
    val tokenExpiredSec = (parseResult.body["exp"] as Int).toLong()
    return Date().time < tokenExpiredSec * 1000
  }

  fun getCurrentUserId(authToken: String): Long {
    val parseResult: Jws<Claims> = parse(authToken)
    return (parseResult.body["userId"] as Int).toLong()
  }

  private fun parse(authToken: String): Jws<Claims> {
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder()
      .setSigningKey(signingKey)
      .build()
      .parseClaimsJws(prefixRemoved)
  }
}