package com.wafflestudio.seminar.core.user.api.response

import java.time.LocalDateTime

data class GetUserResponse (
    val id: Long,
    val username: String,
    val email: String,
    val lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,
    val participant: ParticipantResponse?,
    val instructor: InstructorResponse?,
)