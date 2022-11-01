package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class ParticipatingSeminarInfo(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?,
)

data class InstructingSeminarInfo(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime,
)