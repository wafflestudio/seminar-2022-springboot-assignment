package com.wafflestudio.seminar.core.userSeminar.domain

import java.time.LocalDateTime

data class UserInstructorSeminar(
    val seminarId: Long,
    val seminarName: String,
    val joinedAt: LocalDateTime?
) {
}