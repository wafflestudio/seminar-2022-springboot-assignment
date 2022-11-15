package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity

data class JoinSeminarRequest(
    val role: UserSeminarEntity.Role
)