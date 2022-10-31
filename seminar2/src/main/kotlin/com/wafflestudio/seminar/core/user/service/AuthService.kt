package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.UserRepositorySupport
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface AuthService {
    fun signUp(request: SignUpRequest): AuthToken
    fun logIn(request: LogInRequest): AuthToken
    fun getMe(userId: Long): User
}

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authTokenService: AuthTokenService,
    private val userRepositorySupport: UserRepositorySupport,
) : AuthService {
    
    @Transactional
    override fun signUp(request: SignUpRequest): AuthToken {
        val user = UserEntity(
            email = request.email,
            username = request.username,
            password = passwordEncoder.encode(request.password),
        )
        try {
            userRepository.save(user)
            return authTokenService.generateTokenByUserId(user.id)
        } catch (e: DataIntegrityViolationException) {
            throw Seminar409(request.email + "은(는) 이미 있는 이메일입니다")
        }
    }

    @Transactional
    override fun logIn(request: LogInRequest): AuthToken {
        val user = userRepository.findByEmail(request.email)
            ?: throw Seminar404(request.email + "은(는) 없는 이메일입니다")
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw AuthException("비밀번호가 틀렸습니다")
        }
        userRepositorySupport.lastLogInTime(user.id)
        return authTokenService.generateTokenByUserId(user.id)

    }

    @Transactional
    override fun getMe(userId: Long): User {
        return userRepository.findByIdOrNull(userId)?.toUser()
            ?: throw AuthException("잘못된 유저에 대한 토큰입니다")
    }
}