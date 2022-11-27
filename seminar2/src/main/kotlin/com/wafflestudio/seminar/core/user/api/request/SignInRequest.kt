package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank

data class SignInRequest(
    @field:NotBlank(message = "해당 정보가 없습니다.")
    val email: String,

    @field:NotBlank(message = "해당 정보가 없습니다.")
    val password: String,
)