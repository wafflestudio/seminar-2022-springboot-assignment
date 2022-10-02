package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotBlank

data class CreateUserRequest(
    @NotBlank
    val userName: String,
    @NotBlank
    val email: String,
    @NotBlank
    val password: String,
) {
    fun toUser(passwordEncoder: PasswordEncoder) : UserEntity {
        return UserEntity(
            userName = this.userName,
            email = this.email,
            password = passwordEncoder.encode(this.password)
        )
    }
}

data class SignInUserRequest(
    @NotBlank
    val email: String,
    @NotBlank
    val password: String,
)

