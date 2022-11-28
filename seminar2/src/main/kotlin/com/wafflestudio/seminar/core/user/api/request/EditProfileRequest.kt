package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.Positive

data class EditProfileRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val company: String? = null,
    @field:Positive(message = "0 이상의 값을 입력해주세요.")
    val year: Int? = null,
    val university: String? = null,
    val isRegistered: Boolean? = null
)