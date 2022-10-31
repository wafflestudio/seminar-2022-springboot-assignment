package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignInRequest (
    @field:NotBlank(message= "이메일은 필수 입력값입니다.")
    @field:Email(message= "잘못된 이메일 형식입니다.")
    val email: String?,
    @field:NotBlank(message= "비밀번호는 필수 입력값입니다.")
    val password: String?,
)