package com.wafflestudio.seminar.core.seminar.api.dto

import com.wafflestudio.seminar.core.user.domain.UserRole
import javax.validation.constraints.NotNull

data class JoinSeminarRequest(
    @field: NotNull
    val role: UserRole
)