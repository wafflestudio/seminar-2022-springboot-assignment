package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.Role
import javax.validation.constraints.*

data class SignUpRequest(
    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "email is required")
    val email: String?,
    @field:NotBlank(message = "username is required")
    val username: String?,
    @field:NotBlank(message = "password is required")
    val password: String?,
    @field:NotNull(message = "role is required")
    val role: Role?,
    val university: String = "",
    val isRegistered: Boolean = true,
    val company: String = "",
    @field:PositiveOrZero(message = "year cannot be negative")
    val year: Int? = null
) {
    fun toUserEntity(): UserEntity {
        val userEntity = UserEntity(email!!, username!!, password!!)
        if (role == Role.PARTICIPANT) {
            val participantProfileEntity = ParticipantProfileEntity(
                university, isRegistered
            )
            participantProfileEntity.addUser(userEntity)
        } else {
            val instructorProfileEntity = InstructorProfileEntity(
                company, year
            )
            instructorProfileEntity.addUser(userEntity)
        }
        return userEntity
    }
}