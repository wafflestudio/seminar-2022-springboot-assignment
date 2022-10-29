package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class UserSeminar(
    val joinedAt: LocalDateTime,
    val droppedAt: LocalDateTime? = null,
    var isActive: Boolean,
)