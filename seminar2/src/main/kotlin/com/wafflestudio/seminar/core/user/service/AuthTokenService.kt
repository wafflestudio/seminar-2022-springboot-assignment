package com.wafflestudio.seminar.core.user.service

import io.jsonwebtoken.*
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

  
  fun generateTokenByUsername(email: String): AuthToken {
    val claims: MutableMap<String, Any>
    val cal : Calendar = Calendar.getInstance()
    cal.add(Calendar.SECOND, authProperties.jwtExpiration.toInt())
    
    
    val expiryDate: Date = Date(cal.timeInMillis)
    val now : Date = Date()
    val resultToken = Jwts.builder()
      .setHeaderParam("type", "JWT")
      .claim("email", email)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(signingKey)
      .compact()
      
    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String): Boolean {
    return try{
      parse(authToken)
      true
    } catch (e: JwtException){
      false
    } catch( e: ExpiredJwtException){
      false
    }
  }

  fun getCurrentUserId(authToken: String): String {
      
    try{
      val claims: Jws<Claims> =  parse(authToken)
      return claims.body.get("email", String::class.java)
    }catch (e : MalformedJwtException){
      throw AuthException("토큰이 변조되었습니다.")

    }catch (e: ExpiredJwtException){
      throw AuthException("토큰이 만료되었습니다.")
    }
    
  }

  /**
   * TODO Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
   *   적절한 인증 처리가 가능하도록 구현해주세요!
   */
  private fun parse(authToken: String): Jws<Claims> {
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
  }
}