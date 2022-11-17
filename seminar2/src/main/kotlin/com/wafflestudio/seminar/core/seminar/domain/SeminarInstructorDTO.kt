package com.wafflestudio.seminar.core.seminar.domain

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import java.time.LocalDateTime

data class SeminarInstructorDTO (
    var id: Long,               // User id
    var username: String,       // User username
    var email: String,          // User email
    var joinedAt: LocalDateTime // UserSeminar joinedAt
) {
    companion object {
        fun of (
            user: UserEntity,
            instructingSeminar: UserSeminarEntity
        ) = user.run {
            SeminarInstructorDTO(
                id = this.id,
                username = this.username,
                email = this.email,
                joinedAt = instructingSeminar.joinedAt!!
            )
        }
    }
}