package com.wafflestudio.seminar.core.user.dto.seminar

import com.wafflestudio.seminar.core.user.domain.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.domain.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity

data class UserProfileDto(
    val userEntity: UserEntity?,
    val participantProfileEntity: ParticipantProfileEntity?,
    val instructorProfileEntity: InstructorProfileEntity?
) {
}