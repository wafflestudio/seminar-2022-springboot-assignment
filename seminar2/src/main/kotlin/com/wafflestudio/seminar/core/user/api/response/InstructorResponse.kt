package com.wafflestudio.seminar.core.user.api.response

import java.time.LocalDateTime

data class InstructorResponse (
    val id: Long,
    val company: String,
    val year: Long?,
    val instructingSeminars: InstructingSeminarResponse?,
)