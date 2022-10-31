package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.database.InstructorProfile
import com.wafflestudio.seminar.core.user.database.ParticipantProfile
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime

data class SignUpRequest(
    val username: String,
    val email: String,
    var password: String,
    val role : String,
    val university : String = "",
    val isRegistered : Boolean = true,
    val company : String = "",
    val year : Int? = null
) {
    fun toUserEntity() : UserEntity {
        val ret = UserEntity(
            username = username,
            email = email,
            password = password,
            lastLogin = LocalDateTime.now(),
            dateJoined = LocalDateTime.now()
        )
        if(role == "Participant") {
            ret.participantProfile = ParticipantProfile(
                university = university,
                isRegistered = isRegistered,
                seminars = listOf()
            )
        }
        else {
            ret.instructorProfile = InstructorProfile(
                company = company,
                year = year,
                seminars = listOf()
            )
        }
        return ret
    }
}