package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.dto.auth.InstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.auth.ParticipantProfileDto

data class SignUpRequest(
    val email: String,
    val username: String,
    var password: String,
    val role: String,

    val participant: ParticipantProfileDto?,
    val instructor: InstructorProfileDto?
)