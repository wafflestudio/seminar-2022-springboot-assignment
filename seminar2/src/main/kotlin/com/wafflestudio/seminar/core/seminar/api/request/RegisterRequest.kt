package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.user.domain.enums.RoleType

data class RegisterRequest (
    /*@field: NotBlank(message="유형 값을 입력해주세요.")
    @field: Pattern(
        regexp="PARTICIPANT|INSTRUCTOR",
        message="올바른 유형 값을 입력해주세요."
    )*/
    val role: RoleType
)