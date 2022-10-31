package com.wafflestudio.seminar.core.user.domain.seminarResponse

import java.time.LocalDateTime

data class InstructorSeminarResponse(
    val id : Long,
    val username : String,
    val email : String,
    val joinedAt : LocalDateTime
)
