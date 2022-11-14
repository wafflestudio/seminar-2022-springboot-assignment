package com.wafflestudio.seminar.core.user.dto

import javax.validation.constraints.Positive

data class ModifyUserRequest(
    val username: String = "",
    val password: String = "",
    val university: String? = null,
    val company: String? = null,
    @field: Positive
    val year: Int? = null,
)