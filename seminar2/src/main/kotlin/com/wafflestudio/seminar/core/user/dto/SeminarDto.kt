package com.wafflestudio.seminar.core.user.dto

import java.time.LocalDateTime

data class SeminarDto(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime?,
    val isActive: Boolean?,
    val droppedAt: LocalDateTime?
    
) {
}