package com.wafflestudio.seminar.core.seminar.dto

import java.time.LocalDateTime

data class InstructingSeminarResponse(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime,
)

data class ParticipantSeminarResponse(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime? = null,
)