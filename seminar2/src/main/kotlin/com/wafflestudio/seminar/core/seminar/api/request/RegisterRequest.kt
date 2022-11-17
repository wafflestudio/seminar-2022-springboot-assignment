package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.common.EnumPattern
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import javax.validation.constraints.NotNull

data class RegisterRequest (
    @field: NotNull(message="회원 유형을 입력해주세요.")
    @field: EnumPattern(
        enumClass = RoleType::class,
        message="회원 유형을 올바르게 입력해주세요."
    )
    val role: String?
)