package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.dto.UpdateInstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.UpdateParticipantProfileDto

data class UpdateProfileRequest(
    val id: Long,
    val username: String?,
    val password: String,
    var participant: UpdateParticipantProfileDto? = null,
    var instructor: UpdateInstructorProfileDto? = null
)