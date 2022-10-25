package com.wafflestudio.seminar.core.user.dto

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import java.time.LocalDateTime

data class UserProfileDto(
    val id: Long,
    val username: String,
    val email: String,
    val dateJoined: LocalDateTime,
    val participant: ParticipantProfileEntity?
) {
}