package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.repository.UserEntity
import java.time.LocalDateTime

data class UserResponse (
    val id: Long,
    val username: String,
    val email: String,
    val lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,
    val participant: ParticipantResponse?,
    val instructor: InstructorResponse?,
) {

    companion object {
        fun of(user: UserEntity): UserResponse { 
            return UserResponse(
                id = user.id,
                username = user.username,
                email = user.email,
                lastLogin = user.lastLogin!!,
                dateJoined = user.createdAt!!,
                participant = ParticipantResponse.of(user),
                instructor = InstructorResponse.of(user)
            )
        }
    }
}
