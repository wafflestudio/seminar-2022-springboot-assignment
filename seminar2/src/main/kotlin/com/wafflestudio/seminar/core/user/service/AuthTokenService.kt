package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.ErrorCode
import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.user.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.security.SignatureException
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
    private val userRepository: UserRepository,
) {
  private val tokenPrefix = "Bearer "
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())
  
  fun generateTokenByEmail(email: String): AuthToken {

    val header: MutableMap<String, Any> = mutableMapOf("typ" to "JWT", "alg" to "HS256")
    
    val claim: MutableMap<String, Any> = mutableMapOf("email" to email)
    val issuedDate: Date = Date()
    val expiryDate: Date = Date(issuedDate.time + authProperties.jwtExpiration * 1000) // millisec 단위

    val resultToken = Jwts.builder()
      .setHeader(header)                 // header
      .setClaims(claim)                  // paylod
      .setIssuer(authProperties.issuer)
      .setIssuedAt(issuedDate)
      .setExpiration(expiryDate)
      .signWith(signingKey)              // 서명
      .compact() 

    return AuthToken(resultToken)
  }

  fun getCurrentUserId(authToken: String): Long {
    val email: String = verifyToken(authToken)
    return userRepository.findByEmail(email)!!.id
  }
  
  fun verifyToken(authToken: String): String {
    val prefixRemoved: String = removePrefix(authToken)
    
    try {
      val claim: Jws<Claims> = Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(prefixRemoved)
      
      val email: String = claim.body.get("email", String::class.java)
      if(!userRepository.existsByEmail(email)){
        // 실제로 DB 상에 존재하는 이메일인지 확인
        throw SeminarException(ErrorCode.EMAIL_NOT_FOUND)
      } else return email
      
    } catch (e: SignatureException) {       // 서명 key가 잘못되었을 때
      throw SeminarException(ErrorCode.FORBIDDEN)
    } catch (e: IllegalArgumentException) { // claimJws string이 null일 때
      throw SeminarException(ErrorCode.FORBIDDEN)
    } catch (e: ExpiredJwtException) {      // 토큰 유효 시간 넘겼을 때
      throw SeminarException(ErrorCode.EXPIRED_TOKEN)
    }
    
  }
  

  private fun removePrefix(authToken: String): String {
    if(!authToken.startsWith(tokenPrefix))
      throw SeminarException(ErrorCode.NOT_BEARER_TYPE)
    return authToken.replace(tokenPrefix, "").trim { it <= ' ' }
  }
}