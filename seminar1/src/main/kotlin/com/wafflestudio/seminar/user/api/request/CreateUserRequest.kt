package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CreateUserRequest(
    @field: NotBlank(message = "별명을 입력해 주세요.")
    val nickname: String,
    @field: Email(message = "유효한 이메일 주소가 아닙니다.")
    @field: NotBlank(message = "이메일을 입력해 주세요.")
    val email: String,
    @field: NotBlank(message = "비밀번호를 입력해 주세요.")
    val password: String,
)
