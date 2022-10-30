package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authTokenService: AuthTokenService,
) {
    fun signUp(request: SignUpRequest): AuthToken {
        val user = UserEntity(
            email = request.email,
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = request.role,
        )
        try {
            userRepository.save(user)
            return authTokenService.generateTokenByUserId(user.id)
        } catch (e: DataIntegrityViolationException) {
            throw Seminar409(request.email + "은(는) 이미 있는 이메일입니다")
        }
    }

    fun logIn(request: LogInRequest): AuthToken {
        try {
            val user = userRepository.findByEmail(request.email)
            if (!passwordEncoder.matches(request.password, user.password)) {
                throw AuthException("비밀번호가 틀렸습니다")
            }
            return authTokenService.generateTokenByUserId(user.id)
        } catch (e: EmptyResultDataAccessException) {
            throw Seminar404(request.email + "은(는) 없는 이메일입니다")
        }
    }

    fun getMe(request: AuthToken): User {
        val userId = authTokenService.getCurrentUserId(request.accessToken)
        return userRepository.findByIdOrNull(userId)?.toUser() 
            ?: throw AuthException("잘못된 유저에 대한 토큰입니다")
    }
}