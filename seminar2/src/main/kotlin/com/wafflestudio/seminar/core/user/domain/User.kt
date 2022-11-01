package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,
    val participant: Participant?,
    val instructor: Instructor?,
)