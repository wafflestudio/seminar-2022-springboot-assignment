package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank

data class LoginRequest (
    @field: NotBlank(message = "email을 입력해주세요.")
    val email: String,
    
    @field: NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String,
)
