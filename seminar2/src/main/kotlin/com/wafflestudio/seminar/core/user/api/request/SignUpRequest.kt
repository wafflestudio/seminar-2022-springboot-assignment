package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.InstructorProfile
import com.wafflestudio.seminar.core.user.database.ParticipantProfile
import com.wafflestudio.seminar.core.user.database.Role
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.jetbrains.annotations.*
import java.time.LocalDateTime

data class SignUpRequest(
    @NotNull("이름을 입력해주세요.")
    val username: String,
    @NotNull("이메일을 입력해주세요.")
    val email: String,
    @NotNull("비밀번호를 입력해주세요.")
    var password: String,
    val role : Role,
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
            lastLogin = LocalDateTime.now()
        )
        if(role == Role.Participant) {
            ret.participantProfile = ParticipantProfile(
                university = university,
                isRegistered = isRegistered
            )
        }
        else if(role == Role.Instructor) {
            ret.instructorProfile = InstructorProfile(
                company = company,
                year = year
            )
        }
        else throw Seminar400("잘못된 역할입니다.")
        return ret
    }
}