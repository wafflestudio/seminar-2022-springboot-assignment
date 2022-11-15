package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.user.type.UserRole

data class ParticipateSeminarRequest(
    val role: UserRole,
)
