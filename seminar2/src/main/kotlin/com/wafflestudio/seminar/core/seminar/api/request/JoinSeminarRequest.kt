package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.user.database.Role

data class JoinSeminarRequest(
    val role: Role
)