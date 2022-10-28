package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.User401
import com.wafflestudio.seminar.core.user.database.UserRepository
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

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
    fun generateTokenByUsername(email: String): AuthToken {
        val claims: MutableMap<String, Any> = HashMap()
        val expiryDate: LocalDateTime = LocalDateTime.now().plusSeconds(authProperties.jwtExpiration)

        claims["username"] = userRepository.findByEmail(email)!!.username
        claims["email"] = email

        val resultToken = Jwts.builder()
            .setSubject(email)
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(signingKey)
            .compact()

        return AuthToken(resultToken)
    }

    fun verifyToken(authToken: String) {
        try {
            val claims = parse(authToken).body
        } catch (e: MalformedJwtException) {
            throw User401("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            throw User401("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            throw User401("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            throw User401("JWT 토큰이 잘못되었습니다.")
        } 
    }

    fun getCurrentUserId(authToken: String): Long {
        val claims = parse(authToken).body
        val email = claims["email"]
        val user = userRepository.findByEmail(email.toString())
        return user!!.id
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