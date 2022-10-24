package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UpdateUserRequest(
    val username: String? = null,
    var university: String = "",
    val company: String = "",
    val year: Int? = null
) {
}