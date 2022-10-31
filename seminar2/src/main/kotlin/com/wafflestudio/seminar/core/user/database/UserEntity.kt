package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.userResponse.UserResponse
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name="users")
class UserEntity(
    val username: String,
    val email: String,
    val password: String,
    var lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,
    @OneToOne
    var participantProfile: ParticipantProfile? = null,
    @OneToOne
    var instructorProfile: InstructorProfile? = null
) : BaseTimeEntity() {
    fun toUserResponse() : UserResponse {
        return UserResponse(
            id = id,
            username = username,
            email = email,
            lastLogin = lastLogin,
            dateJoined = dateJoined,
            participant = participantProfile!!.toParticipantProfileResponse(),
            instructor = instructorProfile!!.toInstructorProfileResponse()
        )
    }
}
