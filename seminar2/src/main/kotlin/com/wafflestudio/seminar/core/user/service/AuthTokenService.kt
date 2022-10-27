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

  fun generateTokenByUsername(username: String): AuthToken {
    println(username)
    val claims: MutableMap<String, Any> = HashMap()
    claims["username"] = username
    val now = Date()
    val expiryDate = Date(now.time + authProperties.jwtExpiration)
    println(expiryDate)
    
    val resultToken = Jwts.builder()
      .setClaims(claims)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(signingKey)
      .compact()
    println(resultToken)

    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String) {
    // TODO()
  }

  fun getCurrentUserId(authToken: String): Long {
    // TODO()
    return 0
  }

  /**
   * TODO Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
   *   적절한 인증 처리가 가능하도록 구현해주세요!
   */
  private fun parse(authToken: String): Jws<Claims> {
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder().build().parseClaimsJws(prefixRemoved)
  }
}