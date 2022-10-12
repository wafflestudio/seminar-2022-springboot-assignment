package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class UserSignup(
    val username: String,
    val email: String,
    val password: String,
    val dateJoined: LocalDateTime,
    val role: String,
    
    val participant: ParticipantProfile?,
    val instructor: InstructorProfile?
)