package com.wafflestudio.seminar.core.user.dto.auth

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class InstructorProfileDto(
    val company: String? = "",
    @field:Positive(message="연차에 양수를 입력해야 합니다")
    val year: Int? = null

) {
}