package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.Role

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val role: Role,
    val university: String = "",
    val isRegistered: Boolean = true,
    val company: String = "",
    val year: Int? = null
) {
    fun toUserEntity(): UserEntity {
        val userEntity = UserEntity(email, username, password)
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