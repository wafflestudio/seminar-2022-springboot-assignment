package com.wafflestudio.seminar.core.user.service

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.time.Instant.now
import java.time.temporal.ChronoUnit
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
  private val authProperties: AuthProperties,
) {
  private val tokenPrefix = "Bearer "
  private val expireTime = authProperties.jwtExpiration
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

  /**
   * TODO Jwts.builder() 라이브러리를 통해서, 어떻게 필요한 정보를 토큰에 넣어 발급하고,
   *   검증할지, 또 만료는 어떻게 시킬 수 있을지 고민해보아요.
   */
  fun generateTokenByEmail(email: String): AuthToken {
    val resultToken = Jwts.builder()
      .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
      .setExpiration(Date.from(now().plus(expireTime, ChronoUnit.SECONDS)))
      .claim("email", email)
      .signWith(signingKey)
      .compact() 

    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String) = try {
    val claims = parse(authToken)
  } catch (e: ExpiredJwtException) {
    throw AuthException("만료된 토큰입니다.")
  }
  
  fun getCurrentEmail(authToken: String): String {
    verifyToken(authToken)
    val claims = parse(authToken)
    return claims.body["email"].toString()
  }

  /**
   * TODO Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
   *   적절한 인증 처리가 가능하도록 구현해주세요!
   */
  private fun parse(authToken: String): Jws<Claims> {
    if(!authToken.startsWith(tokenPrefix)) throw AuthException("헤더 형식이 잘못되었습니다.")
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    try {
      return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
              ?: throw AuthException("유효하지 않은 토큰입니다.")
    } catch (ex:JwtException) {
      throw AuthException("유효하지 않은 토큰입니다.")
    }
  }
}