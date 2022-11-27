package com.wafflestudio.seminar.core.user.dto.seminar

import java.time.LocalDateTime

data class TeacherDto(
    val id: Long?,
    val username: String?,
    val email: String?,
    val joinedAt: LocalDateTime?
)
