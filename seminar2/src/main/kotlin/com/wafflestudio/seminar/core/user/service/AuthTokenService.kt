package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
  private val authProperties: AuthProperties,
  private val userRepository: UserRepository,
) {
  private val tokenPrefix = "Bearer "
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

  fun generateTokenByEmail(email: String): AuthToken {
    val claims: MutableMap<String, Any> = hashMapOf("email" to email)
    val now = Date()
    val expireAt = now.time + authProperties.jwtExpiration * 1000
    val expiryDate = Date(expireAt)
    val resultToken = Jwts.builder()
      .setClaims(claims)
      .setIssuer(authProperties.issuer)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(signingKey, SignatureAlgorithm.HS256)
      .compact()

    return AuthToken(resultToken)
  }

  fun getCurrentUserId(authToken: String): Long {
    verifyToken(authToken)
    val email = parse(authToken).body["email"].toString()
    val user = userRepository.findByEmail(email) ?: throw Seminar401()
    return user.id
  }

  private fun verifyToken(authToken: String) {
    try {
      parse(authToken)
    } catch (ex: ExpiredJwtException) {
      throw Seminar401("만료된 토큰입니다.")
    } catch (ex: Exception) {
      throw Seminar401("잘못된 인증입니다.")
    }
  }

  private fun parse(authToken: String): Jws<Claims> {
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder()
      .requireIssuer(authProperties.issuer)
      .setSigningKey(signingKey)
      .build()
      .parseClaimsJws(prefixRemoved)
  }
}