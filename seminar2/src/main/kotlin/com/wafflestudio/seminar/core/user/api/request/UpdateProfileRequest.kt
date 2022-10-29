package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.dto.user.UpdateInstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.user.UpdateParticipantProfileDto

data class UpdateProfileRequest(
    val username: String?,
    val password: String,
    var participant: UpdateParticipantProfileDto? = null,
    var instructor: UpdateInstructorProfileDto? = null
)