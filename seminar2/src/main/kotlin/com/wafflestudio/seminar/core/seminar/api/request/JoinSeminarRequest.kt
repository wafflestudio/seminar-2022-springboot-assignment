package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import java.time.LocalTime

data class JoinSeminarRequest(
    val role: UserSeminarEntity.Role
)