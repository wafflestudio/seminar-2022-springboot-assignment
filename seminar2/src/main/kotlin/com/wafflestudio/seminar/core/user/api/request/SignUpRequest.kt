package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class SignUpRequest(
    @field:NotBlank(message = "해당 정보가 없습니다.")
    val email: String?,

    @field:NotBlank(message = "해당 정보가 없습니다.")
    val username: String?,

    @field:NotBlank(message = "해당 정보가 없습니다.")
    val password: String?,

    @field:Pattern(regexp = "PARTICIPANT|INSTRUCTOR", message = "역할은 PARTICIPANT 또는 INSTRUCTOR 이어야 합니다.")
    val role: String?,

    val university: String? = "",
    val isRegistered: Boolean? = true,
    val company: String? = "",

    @field:Positive(message = "경력은 양수여야 합니다.")
    val year: Int? = null
)