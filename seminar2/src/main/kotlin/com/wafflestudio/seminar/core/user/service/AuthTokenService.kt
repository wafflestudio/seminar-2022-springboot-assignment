package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.*
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

  fun generateTokenByUsername(username: String): AuthToken {
    val claims: MutableMap<String, Any> = hashMapOf("username" to username)
    val now = Date()
    val expiryDate: Date = Date(now.time + authProperties.jwtExpiration * 1000)
    val resultToken = tokenPrefix + Jwts.builder().setClaims(claims)
      .setIssuer(authProperties.issuer)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(signingKey)
      .compact()
    return AuthToken(resultToken)
  }

  fun generateTokenByEmail(email: String): AuthToken {
    val claims: MutableMap<String, Any> = hashMapOf("email" to email)
    val now = Date()
    val expiryDate: Date = Date(now.time + authProperties.jwtExpiration * 1000)
    val resultToken = tokenPrefix + Jwts.builder().setClaims(claims)
      .setIssuer(authProperties.issuer)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(signingKey)
      .compact()
    return AuthToken(resultToken)
  }

  fun verifyToken(authToken: String?) : Boolean{
    if (authToken.isNullOrEmpty()) {
      throw AuthException("Token is not given.")
    }
    if (!authToken.startsWith(tokenPrefix)) {
      throw AuthException("Token is not a bearer type.")
    }
    val jwsClaims : Jws<Claims> = parse(authToken)
    val email = jwsClaims.body.get("email", String::class.java)
    if (email.isNullOrEmpty()) {
      throw AuthException("Token is not valid.")
    }
    return true
  }

  fun getCurrentUserId(authToken: String?): Long {
    if (authToken.isNullOrEmpty()) {
      throw AuthException("Token is not given.")
    }
    val jwsClaims : Jws<Claims> = parse(authToken)
    val email = jwsClaims.body.get("email", String::class.java)
    val userEntity = userRepository.findByEmail(email) ?: throw AuthException("Token is not valid.")
    return userEntity.id
  }


  private fun parse(authToken: String): Jws<Claims> {
    val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
    return try {
      Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
    } catch (e: SecurityException) {
      throw AuthException("Signature is not valid.")
    } catch (e: MalformedJwtException) {
      throw AuthException("Token is not valid.")
    } catch (e: ExpiredJwtException) {
      throw AuthException("Token's expiration date is over. You should log in again.")
    } catch (e: UnsupportedJwtException) {
      throw AuthException("Unsupported token.")
    } catch (e: IllegalArgumentException) {
      throw AuthException("Token's claim is empty.")
    }
  }
}