package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class CreateUserRequest(
        @field: NotBlank(message = "nickname field should not be blank.")
        val nickname: String? = null,
        @field: NotBlank(message = "email field should not be blank.")
        val email: String? = null,
        @field: NotEmpty(message = "password field should not be empty.")
        val password: String? = null,
)