package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.NotNull

data class UserLoginRequest(
        @field: NotNull(message = "email field should be given.")
        val email: String? = null,
        @field: NotNull(message = "password field should be given.")
        val password: String? = null
)