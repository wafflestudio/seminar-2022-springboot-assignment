package com.wafflestudio.seminar.core.user.domain

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.user.domain.profile.InstructorDTO
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantDTO
import java.time.LocalDate
import java.time.LocalDateTime

data class UserDTO (
    var id: Long,                    // User id
    var username: String,            // User username
    var email: String,               // User email
    var lastLogin: LocalDateTime,    // User modifiedAt
    var dateJoined: LocalDate,   // User createdAt
    var participant: ParticipantDTO? = null,
    var instructor: InstructorDTO? = null
) {
    companion object {
        fun of(
            entity: UserEntity,
            participantProfile: ParticipantDTO?,
            instructorProfile: InstructorDTO?
        ) = entity.run {
            UserDTO(
                id = this.id,
                username = this.username,
                email = this.email,
                lastLogin = this.lastLogin,
                dateJoined = this.dateJoined!!,
                participant = participantProfile,
                instructor = instructorProfile
            )
        }
    }
}