package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.Date
import javax.servlet.http.HttpServletRequest

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
  private val authProperties: AuthProperties,
  private val userRepository: UserRepository
) {
//  private val tokenPrefix = "Bearer "
  private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

  /**
   *   Jwts.builder() 라이브러리를 통해서, 어떻게 필요한 정보를 토큰에 넣어 발급하고,
   *   검증할지, 또 만료는 어떻게 시킬 수 있을지 고민해보아요.
   */
  fun generateTokenByUsername(username: String): AuthToken {
    // Generate Token by given EMAIL!
    val claims: MutableMap<String, Any> = Jwts.claims()
    claims["email"] = username
    val expiryDate: Date = Date( Date().time + authProperties.jwtExpiration )
    val resultToken = Jwts.builder()
            .setClaims(claims)
            .setIssuer(authProperties.issuer)
            .setExpiration(expiryDate)
            .signWith(signingKey)
            .compact() 

    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String): Boolean {
    return try {
      val jws: Jws<Claims> = parse(authToken) // Check expiration automatically in here.
      val expiryDate: Date = jws.body.expiration
      expiryDate.after(Date())
    } catch (e: JwtException) {
      false
    } catch (e: IllegalArgumentException) {
      false
    }
  }

//  fun getCurrentUserId(authToken: String): Long? {
//    val jws: Jws<Claims> = parse(authToken)
//    val email = jws.body["email"] as String
//    val user = userRepository.findUserEntityByEmail(email)
//    return user?.id
//  }
  
  fun getCurrentUserEmail(authToken: String): String? {
    val jws: Jws<Claims> = parse(authToken)
    return jws.body["email"] as String?
  }


  /**
   *   Jwts.parserBuilder() 빌더 패턴을 통해 토큰을 읽어올 수도 있습니다.
   *   적절한 인증 처리가 가능하도록 구현해주세요!
   */
  private fun parse(authToken: String): Jws<Claims> {
//    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(authToken)
//            .parseClaimsJws(prefixRemoved)
  }
}

class AuthTokenExtractor {

  companion object {
    const val AUTHORIZATION_HEADER_PREFIX = "Authorization"
    const val BEARER_TYPE_PREFIX = "Bearer "

    fun extract(request: HttpServletRequest): String? {
      val authorization: String? = request.getHeader(AUTHORIZATION_HEADER_PREFIX)
      authorization?.let {
        if (authorization.startsWith(BEARER_TYPE_PREFIX)) {
          return authorization.replace(BEARER_TYPE_PREFIX, "").trim { it <= ' ' }
        }
      }
      return null
    }
  }
}
