package com.wafflestudio.seminar.core.user.domain.userResponse

import java.time.LocalDateTime

data class ParticipantProfileSeminarResponse(
    val id : Long,
    val name : String,
    val joinedAt : LocalDateTime,
    val isActive : Boolean,
    val droppedAt : LocalDateTime?
)
