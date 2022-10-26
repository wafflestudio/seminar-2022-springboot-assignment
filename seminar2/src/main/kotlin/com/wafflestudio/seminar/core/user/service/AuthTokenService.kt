package com.wafflestudio.seminar.core.user.service

import io.jsonwebtoken.*
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
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
  
  public val AUTHORIZATION_HEADER_KEY = "Authorization"
  
  fun generateTokenByEmail(email: String) : AuthToken {
    val claims: Claims = Jwts.claims().setSubject(email)
    val currentTime = Date()
    val token: String = Jwts.builder()
      .setHeaderParam("typ", "JWT")
      .setClaims(claims)
      .setIssuedAt(currentTime)
      .setExpiration(Date(currentTime.time + authProperties.jwtExpiration))
      .signWith(signingKey)
      .compact()
    return AuthToken(tokenPrefix + token)
  }

  fun verifyToken(authToken: String) : Claims {
    try {
      val claims = parse(authToken).body
      claims.expiration.after(Date())
      return claims
    } catch (e: ExpiredJwtException) {
      throw AuthException("토큰이 만료됐습니다")
    } catch (e: SignatureException) {
      throw AuthException("잘못된 토큰입니다")
    } catch (e: DecodingException) {
      throw AuthException("토큰 형식이 잘못됐습니다. 띄어쓰기나 JWT prefix 형식이 잘못됐는지 확인하세요")
    } catch (e: JwtException) {
      throw AuthException("예상하지 못한 JWT 에러입니다")
    }
  }
  
  fun getCurrentUserEmail(authToken: String): String {
    return verifyToken(authToken).subject
  }
  
  private fun parse(authToken: String): Jws<Claims> {
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
  }
}