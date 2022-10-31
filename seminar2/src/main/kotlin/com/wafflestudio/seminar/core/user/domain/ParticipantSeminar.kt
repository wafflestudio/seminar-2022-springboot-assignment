package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class ParticipantSeminar(
    val seminarId: Long,
    val name: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?,
)