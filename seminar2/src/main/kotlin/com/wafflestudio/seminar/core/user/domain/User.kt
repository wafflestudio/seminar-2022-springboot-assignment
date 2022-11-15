package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val lastLogin: LocalDateTime,
    val dateJoined: LocalDate,
    val participant: ParticipantProfile?,
    val instructor: InstructorProfile?,
) {
    companion object {
        fun of(entity: UserEntity): User = entity.run {
            User(
                id = id,
                username = username,
                email = email,
                lastLogin = lastLoginedAt,
                dateJoined = createdAt!!.toLocalDate(),
                participant = participantProfile?.let(ParticipantProfile::of),
                instructor = instructorProfile?.let(InstructorProfile::of),
            )
        }
    }
}