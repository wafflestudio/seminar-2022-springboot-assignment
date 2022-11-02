package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.time.Instant.now
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDate
import java.time.temporal.ChronoUnit
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
  private val authProperties: AuthProperties,
  private val userRepository: UserRepository
) {
  private val tokenPrefix = "Bearer "
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

  /**
   * TODO Jwts.builder() 라이브러리를 통해서, 어떻게 필요한 정보를 토큰에 넣어 발급하고,
   *   검증할지, 또 만료는 어떻게 시킬 수 있을지 고민해보아요.
   */
  fun generateTokenByEmail(email: String): AuthToken {
    val headers: MutableMap<String, Any> = mutableMapOf(
      Pair("alg", "HS256"),
      Pair("typ", "JWT")
    )
    val claims: MutableMap<String, Any> = mutableMapOf("email" to email)
    val resultToken = Jwts.builder()
      .setHeader(headers)
      .setExpiration(Date.from(now().plus(authProperties.jwtExpiration, ChronoUnit.SECONDS)))
      .setIssuer(authProperties.issuer)
      .addClaims(claims)
      .signWith(signingKey)
      .compact()

    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String) {
    try {
      getCurrentUserId(authToken)
    } catch(e: ExpiredJwtException) {
      throw AuthException("만료된 토큰입니다.")
    }
  }

  fun getCurrentUserId(authToken: String): Long {
    val email = parse(authToken).body["email"].toString()
    val user = userRepository.findByEmail(email) ?: throw AuthException("유효하지 않은 토큰입니다.")
    return user.id
  }

  /**
   * TODO Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
   *   적절한 인증 처리가 가능하도록 구현해주세요!
   */
  private fun parse(authToken: String): Jws<Claims> {
    if(!authToken.startsWith(tokenPrefix)) throw AuthException("Authorization Header의 형식이 잘못되었습니다.")
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved) ?: throw AuthException("유효하지 않은 토큰입니다.")
  }
}