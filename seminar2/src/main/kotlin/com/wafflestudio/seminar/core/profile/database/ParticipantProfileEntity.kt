package com.wafflestudio.seminar.core.profile.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.*

@Entity
@Table(name = "participant_profile")
class ParticipantProfileEntity(
    @Column(nullable = false)
    var university: String,

    @Column(nullable = false)
    var isRegistered: Boolean,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
) : BaseTimeEntity()