package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.NotBlank

data class LoginUserRequest(
    @field:NotBlank(message = "해당 정보가 없습니다.")
    val email: String?,

    @field:NotBlank(message = "해당 정보가 없습니다.")
    val password: String?,
)