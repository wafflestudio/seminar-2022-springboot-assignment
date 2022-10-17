package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
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
    val claims: MutableMap<String, Any> = Jwts.claims().setSubject("access")

    claims["email"] = email
    
    val now = System.currentTimeMillis()
    val nowDate = Date(now) 
    val expiryDate: Date = Date(nowDate.time+Duration.ofDays(1).toMillis())
    val resultToken = Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
      .setClaims(claims)
      .setIssuer("test").setIssuedAt(nowDate).setExpiration(expiryDate)
      .signWith(signingKey, SignatureAlgorithm.HS256).compact()

    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String) {
    TODO()
  }

  fun getCurrentUserId(authToken: String) :Long{
    //parse(authToken)에 저장된 email을 findByEmail에 적용하여 id를 구하나?
    
    val email : String = parse(authToken).body["email"].toString()
    println(userRepository.findByEmail(email).id)
    return userRepository.findByEmail(email).id
    
  }

  fun getCurrentLastLogin(authToken: String) :LocalDateTime{
    //parse(authToken)에 저장된 email을 findByEmail에 적용하여 id를 구하나?

    return parse(authToken).body.issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() // Date -> LocalDateTime
    
  }

  /**
   * TODO Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
   *   적절한 인증 처리가 가능하도록 구현해주세요!
   */
  
  private fun parse(authToken: String): Jws<Claims> {
    // JWT 문자열을 파씽하여 오브젝트로 변환
// 토큰 형식이 유효하지 않을 경우 io.jsonwebtoken.MalformedJwtException 예외 발생
// 토큰 만료시 io.jsonwebtoken.ExpiredJwtException 예외 발생
// 시그너쳐 미일치시 io.jsonwebtoken.security.SignatureException 예외 발생
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder().setSigningKey(signingKey)
      .build().parseClaimsJws(prefixRemoved)
  }
}