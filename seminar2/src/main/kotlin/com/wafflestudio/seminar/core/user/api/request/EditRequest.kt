package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class EditRequest(
    val email: String? = null,
    @field:NotBlank(message= "이름은 공백으로 수정할 수 없습니다.")
    val username: String? = null,
    @field:NotBlank(message= "비밀번호는 공백으로 수정할 수 없습니다.")
    val password: String? = null,
    @field:Pattern(regexp= "INSTRUCTOR|PARTICIPANT", message="유형 값을 올바르게 입력해주세요.")
    val role: String? = null,
    val company: String? = null,
    @field:Positive(message = "0 이상의 값을 입력해주세요.")
    val year: Int? = null,
    val university: String? = null,
    val isRegistered: Boolean? = null
)