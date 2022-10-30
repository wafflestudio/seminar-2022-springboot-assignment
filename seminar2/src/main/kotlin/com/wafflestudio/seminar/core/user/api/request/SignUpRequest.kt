package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.UserEntity

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val role: Role,
    val university: String = "",
    val isRegistered: Boolean = true,
    val company: String = "",
    val year: Long? = null,
) {
    init {
        if (year != null && year < 0) {
            throw Seminar400("연차가 잘못 입력되었습니다.")
        }
    }
    
    fun toUserEntity(encodedPwd: String): UserEntity = when(role) {
        Role.PARTICIPANT -> UserEntity.participant(email, username, encodedPwd, university, isRegistered)
        Role.INSTRUCTOR -> UserEntity.instructor(email, username, encodedPwd, company, year)
    }
    
    
    enum class Role {
        PARTICIPANT, INSTRUCTOR
    }
}