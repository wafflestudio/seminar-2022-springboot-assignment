package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder

data class CreateUserRequest(
    val nickname: String,
    val email: String,
    val password: String,
) {
    fun convertToUser(passwordEncoder: PasswordEncoder) = UserEntity(
            nickname = nickname,
            email = email,
            password = passwordEncoder.encode(password)
    )
}