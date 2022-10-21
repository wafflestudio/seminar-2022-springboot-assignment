package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserEntity(
    var email: String,
    var username: String,
    var password: String,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var participantProfile: ParticipantProfileEntity? = null,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var instructorProfile: InstructorProfileEntity? = null
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val userSeminars: MutableList<UserSeminarEntity> = ArrayList()

    //==연관관계 메서드==//
    fun addUserSeminar(userSeminarEntity: UserSeminarEntity) {
        userSeminars.add(userSeminarEntity)
        userSeminarEntity.user = this
    }

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