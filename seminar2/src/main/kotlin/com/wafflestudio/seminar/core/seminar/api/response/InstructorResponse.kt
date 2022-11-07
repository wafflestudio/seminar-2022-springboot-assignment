package com.wafflestudio.seminar.core.seminar.api.response

import java.time.LocalDateTime

data class InstructorResponse (
        val id: Long,
        val username: String,
        val email: String,
        val joinedAt: LocalDateTime,
)