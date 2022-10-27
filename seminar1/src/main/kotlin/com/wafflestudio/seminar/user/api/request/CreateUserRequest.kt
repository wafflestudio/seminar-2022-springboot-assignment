package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateUserRequest (

    @field:NotBlank @field:NotNull
    val nickname: String,
    
    @field:NotNull
    @field:NotEmpty
    @field:Email
    val email: String,
    
    val password: String,
)