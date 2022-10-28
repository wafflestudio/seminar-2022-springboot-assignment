package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class SeminarForParticipantProfile(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime? = null
)
