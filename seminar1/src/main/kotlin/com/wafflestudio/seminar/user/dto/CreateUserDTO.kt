package com.wafflestudio.seminar.user.dto

import javax.validation.constraints.NotEmpty

data class CreateUserDTO(
    @field:NotEmpty(message = "이름은 필수 입력 값입니다.")
    val nickname: String,
    @field:NotEmpty(message = "이메일은 필수 입력값입니다.")
    val email: String,
    val password: String,
)