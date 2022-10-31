package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.Positive

data class EditProfileRequest(
    val username: String?,
    val university: String = "",
    val company: String = "",

    @field:Positive(message = "경력은 양수여야 합니다.")
    val year: Int? = null
)