package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.NoArg
import java.time.LocalDateTime

@NoArg
data class User(
    var id: Long,
    var username: String,
    var email: String,
    var lastLogin: LocalDateTime,
    var createdAt: LocalDateTime,
    var participant: List<UserSeminar>? = null,
    var instructor: List<UserSeminar>? = null
) {
}