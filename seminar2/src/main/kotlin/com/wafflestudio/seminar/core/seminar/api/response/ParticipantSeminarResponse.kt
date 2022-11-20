package com.wafflestudio.seminar.core.seminar.api.response

import java.time.LocalDateTime

data class ParticipantSeminarResponse(
    val id : Long,
    val username : String,
    val email : String,
    val joinedAt : LocalDateTime,
    val isActive : Boolean,
    val droppedAt : LocalDateTime?
)
