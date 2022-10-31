package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class Participant(
    val userId: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?
)