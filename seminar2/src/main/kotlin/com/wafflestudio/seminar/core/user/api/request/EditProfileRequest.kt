package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.PositiveOrZero

data class EditProfileRequest(
    val username: String? = null,
    val password: String? = null,
    val university: String = "",
    val company: String = "",
    @field:PositiveOrZero(message = "year cannot be negative")
    val year: Int? = null
) {
}