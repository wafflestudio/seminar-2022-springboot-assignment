package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class SeminarParticipantInfo(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?,
)

data class SeminarInstructorInfo(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime,
)