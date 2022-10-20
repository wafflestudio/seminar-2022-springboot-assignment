package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class UserInstructorSeminar(
    val seminarId: Long,
    val seminarName: String,
    val joinedAt: LocalDateTime?
) {
}