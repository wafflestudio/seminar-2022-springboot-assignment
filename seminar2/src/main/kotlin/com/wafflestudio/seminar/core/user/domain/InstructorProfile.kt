package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.seminar.domain.SeminarForInstructorProfile
import java.time.LocalDateTime

data class InstructorProfile(
    val id: Long,
    val company: String? = "",
    val year: Int?,
    val instructingSeminars: SeminarForInstructorProfile?
)
