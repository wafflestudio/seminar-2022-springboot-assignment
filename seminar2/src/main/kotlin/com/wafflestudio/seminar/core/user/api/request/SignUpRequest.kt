package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.dto.InstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.ParticipantProfileDto

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val role: String,

    val participant: ParticipantProfileDto?,
    val instructor: InstructorProfileDto?
)