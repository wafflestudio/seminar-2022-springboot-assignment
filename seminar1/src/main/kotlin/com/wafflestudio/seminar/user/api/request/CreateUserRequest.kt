package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.user.domain.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotBlank

data class CreateUserRequest(
    @field: NotBlank(message = "nickname은 공백일 수 없습니다.")
    val nickname: String,
    @field: NotBlank(message = "email은 공백일 수 없습니다.")
    val email: String,
    @field: NotBlank(message = "password는 공백일 수 없습니다.")
    val password: String,
) {
    fun toUserEntity(
        passwordEncoder: PasswordEncoder
    ): UserEntity {
        return UserEntity(
            nickname = this.nickname,
            email = this.email,
            encodedPassword = passwordEncoder.encode(password)
        )
    }
}