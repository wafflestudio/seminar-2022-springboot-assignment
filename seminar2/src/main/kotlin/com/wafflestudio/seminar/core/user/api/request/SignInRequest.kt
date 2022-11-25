package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class SignInRequest (

    @field:NotNull
    @field:NotEmpty
    @field:Email
    val email: String,

    val password: String,
)