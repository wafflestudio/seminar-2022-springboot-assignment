package com.wafflestudio.seminar.core.seminar.domain

import java.time.LocalDateTime

data class SeminarForInstructorProfile(
    val id: Long,
    val name: String,
    val joinedAt: LocalDateTime
)
