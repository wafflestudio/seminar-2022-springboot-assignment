package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "user")
class UserEntity(
    @Column(unique = true)
    @NotNull
    var email: String,
    @NotNull
    var username: String,
    @NotNull
    var password: String,
    var lastLogin: LocalDateTime? = null,
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