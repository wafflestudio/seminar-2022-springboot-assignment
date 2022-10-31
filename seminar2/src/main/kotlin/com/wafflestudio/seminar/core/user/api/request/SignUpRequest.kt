package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class SignUpRequest(
    @field:NotBlank(message = "이메일은 필수 입력값입니다.")
    @field:Email(message = "잘못된 이메일 형식입니다.")
    val email: String?,
    @field:NotBlank(message = "이름은 필수 입력값입니다.")
    val username: String?,
    @field:NotBlank(message = "비밀번호는 필수 입력값입니다.")
    val password: String?,
    @field:NotNull(message = "회원유형을 선택해주세요.")
    @field:Pattern(
        regexp = "PARTICIPANT|INSTRUCTOR",
        message="유형을 올바른 값으로 입력해주세요."
    ) val role: String?,
    val company: String?,
    @field:Positive(message="양수 값을 입력해주세요.")
    val year: Int?,
    val university: String?,
    val isRegistered: Boolean?
)