package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
class UserEntity(
    val email: String,
    val username: String,
    val password: String,
    @OneToOne(mappedBy = "user")
    val participantProfile: ParticipantProfileEntity?,
    @OneToOne(mappedBy = "user")
    val instructorProfile: InstructorProfileEntity?
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val userSeminars: List<UserSeminarEntity> = ArrayList()
}