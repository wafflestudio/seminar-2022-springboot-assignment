package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class Instructor (
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime,
)