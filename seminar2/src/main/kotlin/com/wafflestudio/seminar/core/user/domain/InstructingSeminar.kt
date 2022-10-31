package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class InstructingSeminar(
    val seminarId: Long,
    val name: String,
    val joinedAt: LocalDateTime,
)