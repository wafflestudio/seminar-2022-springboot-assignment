package com.wafflestudio.seminar.core.user.api.request

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

class UserDto {

    data class SignUpRequest(
        @field:NotBlank(message = "You should write 'email'.")
        val email: String?,
        @field:NotBlank(message = "You should write 'username'.")
        val username: String?,
        @field:NotBlank(message = "You should write 'password'.")
        val password: String?,
        @field:NotBlank(message = "You should write 'role'.")
        val role: String,
        val university: String?,
        val isRegistered: Boolean?,
        val company: String?,
        @field:PositiveOrZero(message = "'year' should be positive or zero.")
        val year: Int?
    )

    data class UpdateRequest(
        val username: String?,
        val password: String?,
        val university: String?,
        val company: String?,
        @field:PositiveOrZero(message = "'year' should be positive or zero.")
        val year: Int?
    )

    data class RegisterParticipantRequest(
        val university: String?,
        val isRegistered: Boolean?
    )

    data class UserResponse(
        val id: Long,
        val username: String,
        val email: String,
        val lastLogin: LocalDateTime
    )

    data class UserProfileResponse @QueryProjection constructor(
        val id: Long,
        val username: String,
        val email: String,
        val lastLogin: LocalDateTime,
        val dateJoined: LocalDateTime,
        var participant: ParticipantProfileResponse?,
        var instructor: InstructorProfileResponse?
    )

    data class ParticipantProfileResponse @QueryProjection constructor(
        val id: Long,
        val university: String?,
        val isRegistered: Boolean?
    ) {
        var seminars: MutableList<SeminarDto.SeminarResponse>?=null
    }

    data class InstructorProfileResponse @QueryProjection constructor(
        val id: Long,
        val company: String?,
        val year: Int?,
    ){
        var instructingSeminars: MutableList<SeminarDto.InstructingSeminarResponse>?=null
    }

    data class SeminarParticipantProfileResponse @QueryProjection constructor(
        val id: Long,
        val username: String,
        val email: String,
        val joinedAt: LocalDateTime,
        val isActive: Boolean,
        val droppedAt: LocalDateTime?
    ) {


    }

    data class SeminarInstructorProfileResponse @QueryProjection constructor(
        val id: Long,
        val username: String,
        val email: String,
        val joinedAt: LocalDateTime
    )


}

