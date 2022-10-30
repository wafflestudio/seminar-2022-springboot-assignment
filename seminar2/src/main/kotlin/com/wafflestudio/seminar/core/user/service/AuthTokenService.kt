package com.wafflestudio.seminar.core.user.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.Instant.*
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.exp

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
  private val authProperties: AuthProperties,
) {
  private val tokenPrefix = "Bearer "
  private val expireTime = authProperties.jwtExpiration
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

  fun generateTokenByUsername(username: String): AuthToken {
    val headers: MutableMap<String, Any> = mutableMapOf(
      Pair("alg", "HS256"),
      Pair("typ", "JWT")
    )

    val payloads = mutableMapOf(
      Pair("username", username),
      Pair("exp", Date.from(now().plus(expireTime, ChronoUnit.MILLIS)))
    )
    
    val token = Jwts.builder()
      .setHeader(headers)
      .setClaims(payloads)
      .signWith(signingKey)
      .compact()

    return AuthToken(token)
  }

  fun verifyToken(authToken: String) {
    parse(authToken)
  }

  fun getCurrentUserEmail(authToken: String): String {
    return parse(authToken).body["username"] as String
  }
  
  private fun parse(authToken: String): Jws<Claims> {
    if (!authToken.startsWith("Bearer"))
      throw JwtException("Authorization header가 Bearer로 시작하지 않습니다.")
    
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    
    return Jwts.parserBuilder()
      .setSigningKey(signingKey)
      .build()
      .parseClaimsJws(prefixRemoved)
  }
}