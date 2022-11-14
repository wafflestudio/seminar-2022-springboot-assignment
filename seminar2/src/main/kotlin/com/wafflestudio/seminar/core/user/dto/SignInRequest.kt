package com.wafflestudio.seminar.core.user.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class SignInRequest(
    @field: NotBlank(message = "Email should not be empty")
    @field: Email(message = "Not an email format.")
    val email: String? = null,
    @field: NotEmpty(message = "Password should not be empty.")
    val password: String? = null
)