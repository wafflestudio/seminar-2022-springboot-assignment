package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class Participant (
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime,
    val droppedAt: LocalDateTime?,
    val isActive: Boolean,
)