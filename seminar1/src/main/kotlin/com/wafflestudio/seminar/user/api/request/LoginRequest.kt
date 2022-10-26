package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "이메일을 입력하세요")
    @field:Email(message = "올바른 이메일을 입력하세요")
    val email: String,
    
    @field:NotBlank(message = "비밀번호를 입력하세요")
    val password: String,
)