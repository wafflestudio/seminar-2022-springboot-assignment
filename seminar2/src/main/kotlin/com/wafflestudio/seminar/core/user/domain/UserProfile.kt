package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import java.time.LocalDateTime

data class UserProfile(
    val id: Long,
    val username: String,
    val email: String,
    val lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,
    val participant: ParticipantProfileEntity?,
    val instructor: InstructorProfileEntity?
)