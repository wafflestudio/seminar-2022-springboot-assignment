package com.wafflestudio.seminar.core.user.dto

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime

data class UserProfileDto(
    val userEntity: UserEntity?,
    val participantProfileEntity: ParticipantProfileEntity?,
    val instructorProfileEntity: InstructorProfileEntity?
) {
}