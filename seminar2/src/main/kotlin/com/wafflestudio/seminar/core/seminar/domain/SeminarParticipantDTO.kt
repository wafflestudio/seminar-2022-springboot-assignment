package com.wafflestudio.seminar.core.seminar.domain

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import java.time.LocalDateTime

data class SeminarParticipantDTO (
    var id: Long,                       // User id
    var username: String,               // User username
    var email: String,                  // User email
    var joinedAt: LocalDateTime,        // UserSeminar joinedAt
    var isActive: Boolean,              // UserSeminar isActive
    var droppedAt: LocalDateTime?= null // UserSeminar droppedAt
) {
    companion object {
        fun of (
            user: UserEntity,
            participatedSeminar: UserSeminarEntity
        ) = user.run {
            SeminarParticipantDTO(
                id = this.id,
                username = this.username,
                email = this.email,
                joinedAt = participatedSeminar.joinedAt!!,
                isActive = participatedSeminar.isActive,
                droppedAt = participatedSeminar.droppedAt
            )
        }
    }
}