package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.domain.UserPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserPort {
    override fun createUser(signUpRequest: SignUpRequest): User {
        val username = signUpRequest.username
        val email = signUpRequest.email
        val encodedPassword = passwordEncoder.encode(signUpRequest.password)
        userRepository.findByEmail(email)?.let { throw Seminar409("${email}: 중복된 이메일입니다.") }

        val userEntity = UserEntity(email, username, encodedPassword)
        return userRepository.save(userEntity).toUser()
    }

    override fun getUser(signInRequest: SignInRequest): User {
        val email = signInRequest.email
        val password = signInRequest.password
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("해당 이메일(${email})로 등록된 사용자가 없어요.")

        return if (passwordEncoder.matches(
                password,
                userEntity.encodedPassword
            )
        ) userEntity.toUser() else throw Seminar401("비밀번호가 잘못되었습니다.")
    }
}