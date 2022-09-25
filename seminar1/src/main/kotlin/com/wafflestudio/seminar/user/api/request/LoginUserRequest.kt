package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.NotBlank

data class LoginUserRequest(
    @field: NotBlank(message = "email은 공백일 수 없습니다.")
    val email: String,
    @field: NotBlank(message = "password는 공백일 수 없습니다.")
    val password: String,
)