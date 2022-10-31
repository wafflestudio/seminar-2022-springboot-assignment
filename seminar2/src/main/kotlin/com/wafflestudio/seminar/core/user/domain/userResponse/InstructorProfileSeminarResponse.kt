package com.wafflestudio.seminar.core.user.domain.userResponse

import java.time.LocalDateTime

data class InstructorProfileSeminarResponse(
    val id : Long,
    val name : String,
    val joinedAt : LocalDateTime
)
