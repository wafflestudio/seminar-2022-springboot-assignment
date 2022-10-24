package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.type.UserRole
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class SignUpRequest(
    @field:NotNull
    @field:NotEmpty
    @field:Email
    val email: String,
    
    @field:NotBlank @field:NotNull
    val username: String,

    @field:NotBlank @field:NotNull
    val password: String,
    
    @field:NotNull
    val role: UserRole,
    
    var university: String = "",
    var isRegistered: Boolean = true,
    
    val company: String = "",
    
    val year: Int? = null
)