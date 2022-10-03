package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateUserRequest(
    @field:NotBlank(message = "해당 정보가 없습니다.")
    val nickname: String?,

    @field:NotBlank(message = "해당 정보가 없습니다.")
    val email: String?,
    
    @field:NotBlank(message = "해당 정보가 없습니다.")
    val password: String?,
)