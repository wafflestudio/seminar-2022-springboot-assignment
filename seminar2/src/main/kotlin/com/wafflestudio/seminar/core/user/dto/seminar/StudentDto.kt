package com.wafflestudio.seminar.core.user.dto.seminar

import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity
import java.time.LocalDateTime

data class StudentDto(
        val id: Long?,
        val username: String?,
        val email: String?,
        val joinedAt: LocalDateTime?,
        val isActive: Boolean?,
        val droppedAt: LocalDateTime?
) {
    companion object {
        fun of(
                studentEntity: UserEntity,
                studentSeminarEntity: UserSeminarEntity
        ) = studentEntity.run {
            StudentDto(
                    id = id,
                    username = username,
                    email = email,
                    joinedAt = studentSeminarEntity.joinedAt,
                    isActive = studentSeminarEntity.isActive,
                    droppedAt = studentSeminarEntity.droppedAt
            )
        }
    }
}