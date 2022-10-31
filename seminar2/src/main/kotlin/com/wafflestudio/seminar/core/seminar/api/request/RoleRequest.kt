package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class RoleRequest(
    @field:Enumerated(EnumType.STRING)
    val role: User.Role,
)