package com.wafflestudio.seminar.core.user.domain.userResponse

import java.time.LocalDateTime

data class UserResponse(
    val id : Long,
    val username : String,
    val email : String,
    val lastLogin : LocalDateTime,
    val dateJoined : LocalDateTime,
    val participant : ParticipantProfileResponse?,
    val instructor : InstructorProfileResponse?
)
