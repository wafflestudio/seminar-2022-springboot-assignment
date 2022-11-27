package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "email is required")
    val email: String?,
    @field:NotBlank(message = "password is required")
    val password: String?
) {
}