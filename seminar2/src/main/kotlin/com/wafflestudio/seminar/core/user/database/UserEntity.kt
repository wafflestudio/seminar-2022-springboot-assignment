package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserEntity(
    val email: String,
    val username: String,
    val password: String,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var participantProfile: ParticipantProfileEntity? = null,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var instructorProfile: InstructorProfileEntity? = null
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val userSeminars: List<UserSeminarEntity> = ArrayList()

    //==Mapping DTO==//
    fun toDTO(): User {
        return User(
            id = id,
            username = username,
            email = email,
            lastLogin = null,
            dateJoined = createdAt,
            participant = participantProfile?.toDTO(),
            instructor = instructorProfile?.toDTO()
        )
    }
}