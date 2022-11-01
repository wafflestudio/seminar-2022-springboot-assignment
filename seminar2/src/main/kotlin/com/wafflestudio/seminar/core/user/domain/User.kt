package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.InstructorEntity
import com.wafflestudio.seminar.core.user.database.ParticipantEntity
import com.wafflestudio.seminar.core.user.database.UserEntity

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val participant: ParticipantEntity?,
    val instructor: InstructorEntity?,
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            username = username,
            email = email,
            password = password,
            participant = participant,
            instructor = instructor,
        )
    }
}