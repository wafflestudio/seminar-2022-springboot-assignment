package com.wafflestudio.seminar.core.user.api.response

import java.time.LocalDateTime

data class SeminarResponse (
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?
)