package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
class UserEntity(
    @Column(nullable=false)
    var username: String,
    @Column(nullable=false, unique=true)
    var email: String,
    @Column(nullable=false)
    var password: String,
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var participantProfileEntity: ParticipantProfileEntity?,
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var instructorProfileEntity: InstructorProfileEntity?
) : BaseTimeEntity() {
}