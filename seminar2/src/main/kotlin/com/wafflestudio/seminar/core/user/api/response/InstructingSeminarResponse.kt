package com.wafflestudio.seminar.core.user.api.response

import java.time.LocalDateTime

data class InstructingSeminarResponse (
        val id: Long,
        val name: String,
        val joinedAt: LocalDateTime,
)