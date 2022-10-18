package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
class UserEntity(
    private val email: String,
    private val username: String,
    private val password: String,
    @OneToOne(mappedBy = "user")
    private val participantProfile: ParticipantProfileEntity?,
    @OneToOne(mappedBy = "user")
    private val instructorProfile: InstructorProfileEntity?
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    private val userSeminars: List<UserSeminarEntity> = ArrayList()
}