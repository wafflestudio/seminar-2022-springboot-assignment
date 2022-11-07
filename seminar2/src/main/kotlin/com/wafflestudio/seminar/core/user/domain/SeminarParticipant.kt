package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class SeminarParticipant(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime?,
    val isActive: Boolean,
    val droppedAt: LocalDateTime? = null
) {
}