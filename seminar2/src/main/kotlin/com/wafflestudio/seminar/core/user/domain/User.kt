package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val dateJoined: LocalDateTime? = null,
    val lastLogin: LocalDateTime? = null,
    val participantProfile: ParticipantProfile? = null,
    val instructorProfile: InstructorProfile? = null
)
