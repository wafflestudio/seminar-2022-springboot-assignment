package com.wafflestudio.seminar.core.user.dto

import com.wafflestudio.seminar.core.profile.dto.InstructorProfileResponse
import com.wafflestudio.seminar.core.profile.dto.ParticipantProfileResponse
import java.time.LocalDateTime


data class UserResponse(
        val id: Long,
        val username: String,
        val email: String,
        val lastLogin: LocalDateTime,
        val dateJoined: LocalDateTime,
        val participant: ParticipantProfileResponse? = null,
        val instructor: InstructorProfileResponse? = null,
)