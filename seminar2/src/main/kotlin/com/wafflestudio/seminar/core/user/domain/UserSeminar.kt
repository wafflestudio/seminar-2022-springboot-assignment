package com.wafflestudio.seminar.core.user.domain

import java.time.LocalDateTime

data class UserSeminar(
    val seminarId: Long,
    val seminarName: String,
    val joinedAt: LocalDateTime?,
    val isActive: Boolean,
    val droppedAt: LocalDateTime? = null
) {
}