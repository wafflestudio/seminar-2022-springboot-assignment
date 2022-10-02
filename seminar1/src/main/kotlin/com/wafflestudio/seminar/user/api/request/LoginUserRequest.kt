package com.wafflestudio.seminar.user.api.request

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class LoginUserRequest (
    @field:NotNull(message = "해당 정보가 없습니다.")
    @field:NotEmpty(message = "해당 정보가 비어 있습니다.")
    val email: String?,

    @field:NotNull(message = "해당 정보가 없습니다.")
    @field:NotEmpty(message = "해당 정보가 비어 있습니다.")
    val password: String?,
)