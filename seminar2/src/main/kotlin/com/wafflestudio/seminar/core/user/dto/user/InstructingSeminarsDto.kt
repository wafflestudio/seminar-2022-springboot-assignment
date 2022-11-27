package com.wafflestudio.seminar.core.user.dto.user

import java.time.LocalDateTime

data class InstructingSeminarsDto(
    val id: Long?,
    val name: String?,
    val joinedAt: LocalDateTime?
) {
}