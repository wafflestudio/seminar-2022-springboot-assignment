package com.wafflestudio.seminar.core.user.dto.user

import java.time.LocalDateTime

data class SeminarsDto(
    val id: Long?,
    val name: String?,
    val joinedAt: LocalDateTime?,
    val isActive: Boolean?,
    val droppedAt: LocalDateTime?
) {
}