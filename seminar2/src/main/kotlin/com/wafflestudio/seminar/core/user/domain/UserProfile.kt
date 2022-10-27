package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.NewParticipantProfileEntity1
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.dto.UpdateInstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.UpdateParticipantProfileDto
import java.time.LocalDateTime

data class UserProfile(
    val id: Long,
    val username: String?,
    val password: String,
    var participant: UpdateParticipantProfileDto? = null,
    var instructor: UpdateInstructorProfileDto? = null
)