package com.wafflestudio.seminar.user.domain

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class User(
    val id: Long?,
    val nickname: String,
    val email: String,
    val encodedPassword: String,
)