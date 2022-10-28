package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.domain.UserPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserPort {
    override fun getUser(id: Long): User {
        val entity = userRepository.findByIdOrNull(id.toLong()) ?: throw IllegalArgumentException("USER#$id NOT FOUND!")
        return entity.toUser()
    }

    override fun createUser(signUpRequest: SignUpRequest): User {
        val username = signUpRequest.username
        val email = signUpRequest.email
        val encodedPassword = passwordEncoder.encode(signUpRequest.password)
        userRepository.findByEmail(email)?.let { throw Seminar409("${email}: 중복된 이메일입니다.") }

        val userEntity = UserEntity(email, username, encodedPassword)
        return userRepository.save(userEntity).toUser()
    }
}