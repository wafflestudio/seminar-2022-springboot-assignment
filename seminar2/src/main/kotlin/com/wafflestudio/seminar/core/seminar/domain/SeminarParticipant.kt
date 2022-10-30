package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import java.time.LocalDateTime
import java.time.LocalTime

data class SeminarParticipant(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime,
    val isActive: Boolean,
    val droppedAt: LocalDateTime?,
) {
    companion object {
        fun of(userEntity: UserEntity, userSeminarEntity: UserSeminarEntity) =
            SeminarParticipant(
                id = userSeminarEntity.id,
                username = userEntity.username,
                email = userEntity.email,
                joinedAt = userSeminarEntity.joinedAt,
                isActive = userSeminarEntity.isActive,
                droppedAt = userSeminarEntity.droppedAt,
            )
    }
}