package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class RegRequest (
    /*@field: NotBlank(message="유형 값을 입력해주세요.")
    @field: Pattern(
        regexp="PARTICIPANT|INSTRUCTOR",
        message="올바른 유형 값을 입력해주세요."
    )*/
    val role: RoleType
)