package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class UserInfo(
    val id: Long,
    val username: String,
    val email: String,
    val lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,
    val participant: ParticipantProfile?,
    val instructor: InstructorProfile?,
)