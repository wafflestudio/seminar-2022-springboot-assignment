package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.database.UserRepository
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
  private val userRepository: UserRepository
) {
  private val tokenPrefix = "Bearer "
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

  /**
   * TODO Jwts.builder() 라이브러리를 통해서, 어떻게 필요한 정보를 토큰에 넣어 발급하고,
   *   검증할지, 또 만료는 어떻게 시킬 수 있을지 고민해보아요.
   */
  fun generateTokenByUsername(username: String): AuthToken {
    val claims = Jwts.claims()
    val expiryDate = Date(System.currentTimeMillis() + (authProperties.jwtExpiration * 1000))
    claims["username"] =  username
    val resultToken = Jwts.builder()
      .setClaims(claims)
      .setIssuer(authProperties.issuer)
      .setIssuedAt(Date())
      .setExpiration(expiryDate)
      .signWith(signingKey)
      .compact()

    return AuthToken(resultToken)
  }
  fun verifyToken(authToken: String) {
    val body = parse(authToken).body
    if(body.expiration < Date()){
      throw AuthException("토큰 유효기간이 지났습니다.")
    }
  }

  fun getCurrentUserId(authToken: String): Long {
    val body : Claims = parse(authToken).body
    val email = body.get("username", String::class.java)
    return userRepository.findByEmail(email)?.id ?: throw Seminar404("email 에 해당하는 ID가 없습니다.") 
  }

  /**
   * TODO Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
   *   적절한 인증 처리가 가능하도록 구현해주세요!
   */
  private fun parse(authToken: String): Jws<Claims> {
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder()
      .setSigningKey(signingKey)
      .build()
      .parseClaimsJws(prefixRemoved)
  }
}