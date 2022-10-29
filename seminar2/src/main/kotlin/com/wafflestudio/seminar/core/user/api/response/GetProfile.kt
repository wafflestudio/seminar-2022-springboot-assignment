package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.dto.user.GetProfileInstructorDto
import com.wafflestudio.seminar.core.user.dto.user.GetProfileParticipantDto
import java.time.LocalDate

data class GetProfile(
    val id: Long?,
    val username: String?,
    val email: String?,
    val lastLogin: LocalDate?,
    val dateJoined: LocalDate?,
    val participant: GetProfileParticipantDto?,
    val instructor: GetProfileInstructorDto?

)