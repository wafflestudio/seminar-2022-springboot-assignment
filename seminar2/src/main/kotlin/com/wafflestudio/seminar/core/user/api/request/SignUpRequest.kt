package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank(message = "이메일을 입력하세요")
    @field:Email(message = "올바른 이메일을 입력하세요")
    val email: String,
    @field:NotBlank(message = "이름을 입력하세요")
    val username: String,
    @field:NotBlank(message = "비밀번호를 입력하세요")
    val password: String,
    @field:Enumerated(EnumType.STRING)
    val role: User.Role,
    val university: String = "",
    val isRegistered: Boolean = true,
    val company: String = "",
    @field:Min(1, message = "year는 0 이상의 정수입니다")
    val year: Int?
)