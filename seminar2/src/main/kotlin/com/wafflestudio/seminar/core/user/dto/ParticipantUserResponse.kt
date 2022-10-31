package com.wafflestudio.seminar.core.user.dto

import java.time.LocalDateTime

data class ParticipantUserResponse(
        val id: Long,
        val username: String,
        val email: String,
        val joinedAt: LocalDateTime,
        val isActive: Boolean,
        val droppedAt: LocalDateTime?,
)