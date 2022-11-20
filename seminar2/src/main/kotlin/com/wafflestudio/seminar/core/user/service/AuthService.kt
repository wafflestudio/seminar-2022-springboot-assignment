package com.wafflestudio.seminar.core.user.service;

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service;
import java.time.LocalDate


@Service
class AuthService(
        private val userRepository: UserRepository,
        private val authTokenService: AuthTokenService,
        private val passwordEncoder: PasswordEncoder
) {
    fun signup(request: SignUpRequest): AuthToken {

        if (userRepository.findByEmail(request.email) != null) {
            throw Seminar400("해당 이메일은 이미 가입되어 있습니다")
        }
        val encodedPassword = this.passwordEncoder.encode(request.password)

        if(request.instructor != null){
            if (request.instructor.year != null && request.instructor.year <0 ) {
                throw Seminar400("연도에는 0 또는 양의 정수만 입력할 수 있습니다")
            }
        }
        userRepository.save(UserEntity.of(request, encodedPassword))
        return authTokenService.generateTokenByEmail(request.email)
    }

    fun login(request: LoginRequest): AuthToken {

        val userEntity = userRepository.findByEmail(request.email)
        if (this.passwordEncoder.matches(request.password, userEntity?.password)) {
            val token = authTokenService.generateTokenByEmail(request.email)
            val lastLogin = LocalDate.from(authTokenService.getCurrentIssuedAt(token.accessToken))
            loginEntity(request.email, lastLogin)
            return token
        } else {
            throw Seminar401("인증이 되지 않았습니다")
        }

    }

    

    private fun loginEntity(email: String, lastLogin: LocalDate) {
        val userEntity = userRepository.findByEmail(email)
        userEntity?.lastLogin = lastLogin
        if (userEntity != null) {
            userRepository.save(userEntity)
        }
    }


}
