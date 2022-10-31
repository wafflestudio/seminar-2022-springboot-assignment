package com.wafflestudio.seminar.core.user.domain.seminarResponse

import java.time.LocalDateTime

data class ParticipantSeminarResponse(
    val id : Long,
    val username : String,
    val email : String,
    val joinedAt : LocalDateTime,
    val isActive : Boolean,
    val droppedAt : LocalDateTime?
)
